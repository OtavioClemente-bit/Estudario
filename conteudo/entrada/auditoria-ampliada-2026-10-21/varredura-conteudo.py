"""Varredura determinística de toda a base; produz candidatos para revisão, sem mudar conteúdo."""
from pathlib import Path
from fractions import Fraction
import ast
import collections
import hashlib
import json
import re
import unicodedata

ROOT = Path(__file__).resolve().parents[3]
OUT = Path(__file__).resolve().parent


def norm(value):
    return ' '.join(unicodedata.normalize('NFC', str(value)).casefold().split())


def number(value):
    value = value.replace(' ', '').replace('\u00a0', '')
    if ',' in value:
        value = value.replace('.', '').replace(',', '.')
    elif re.fullmatch(r'[+-]?\d{1,3}(?:\.\d{3})+', value):
        value = value.replace('.', '')
    return Fraction(value)


def numeric_option(text):
    text = text.strip().replace('−', '-')
    match = re.fullmatch(r'(R\$\s*)?([+-]?\d[\d.,\s]*(?:\s*/\s*\d[\d.,]*)?)\s*(%)?', text)
    if not match:
        return None
    raw = match[2]
    try:
        value = number(raw) if '/' not in raw else number(raw.split('/')[0]) / number(raw.split('/')[1])
    except (ValueError, ZeroDivisionError):
        return None
    return ('BRL' if match[1] else 'PERCENT' if match[3] else 'NUMBER', value)


def evaluate(expression):
    if '÷' in expression:
        expression = re.sub(r'(?<![\d.,])(\d+(?:[.,]\d+)*)\s*/\s*(\d+(?:[.,]\d+)*)', r'(\1/\2)', expression)
    expression = expression.strip().replace('×', '*').replace('·', '*').replace('÷', '/').replace('−', '-').replace('^', '**')
    expression = re.sub(r'(?<![\w.])\d[\d.,]*', lambda m: '(' + str(number(m[0])) + ')', expression)
    if len(expression) > 180 or not re.fullmatch(r'[\d\s.+*/()\-]+', expression):
        raise ValueError('Fora da gramatica numerica')
    tree = ast.parse(expression, mode='eval')

    def run(node):
        if isinstance(node, ast.Constant) and type(node.value) is int:
            return Fraction(node.value)
        if isinstance(node, ast.UnaryOp) and isinstance(node.op, (ast.UAdd, ast.USub)):
            value = run(node.operand)
            return value if isinstance(node.op, ast.UAdd) else -value
        if not isinstance(node, ast.BinOp):
            raise ValueError('Operacao nao numerica')
        left, right = run(node.left), run(node.right)
        if isinstance(node.op, ast.Add):
            return left + right
        if isinstance(node.op, ast.Sub):
            return left - right
        if isinstance(node.op, ast.Mult):
            return left * right
        if isinstance(node.op, ast.Div):
            return left / right
        if isinstance(node.op, ast.Pow) and right.denominator == 1 and abs(right) <= 12 and abs(left) < 100000:
            return left ** int(right)
        raise ValueError('Operacao fora dos limites')

    return run(tree.body)


assert evaluate('0,5 ÷ 0,2') == Fraction(5, 2)
assert evaluate('1/2 × 1/2') == Fraction(1, 4)
assert evaluate('3/4 ÷ 5/8') == Fraction(6, 5)
assert evaluate('0,3 × 0,2 + 0,7 × 0,05') == Fraction(19, 200)
assert evaluate('1.200 + 50') == Fraction(1250)
findings = collections.defaultdict(list)
totals = collections.Counter()
fingerprints = {}
conflicts = {}
# Igualdades curtas e puramente numéricas: extração é heurística; aproximações exigem leitura.
equation = re.compile(r'(?<![\w])([\d][\d.,\s()+*/×·÷^−\-]{1,100})\s*=\s*([+-]?\d[\d.,]*(?:\s*/\s*\d[\d.,]*)?)(?![\d])')
letter = re.compile(r'^\s*(?:(?:Gabarito|Resposta)\s*[:\-]?\s*(?:letra\s+)?|(?:A\s+)?alternativa\s+(?:correta\s+(?:é|e)\s+)?)(?-i:([A-E]))\b', re.I)
assert letter.search('Gabarito C. Justificativa.').group(1) == 'C'
assert letter.search('Alternativa E. Justificativa.').group(1) == 'E'
assert letter.search('Resposta a incidentes requer planejamento.') is None
assert letter.search('Alternativa a, sujeito a e vantajoso para.') is None

for folder in ['materias', 'recortes', 'simulados']:
    for path in sorted((ROOT / 'conteudo' / folder).rglob('*.json')):
        if path.name.startswith('_'):
            continue
        data = json.loads(path.read_text())
        totals[folder + '_arquivos'] += 1
        for index, question in enumerate(data.get('questions', []), 1):
            totals[folder + '_questoes'] += 1
            options = question.get('options', [])
            correct = [o for o in options if o.get('correct') is True]
            if len(correct) != 1:
                continue
            loc = {'arquivo': str(path.relative_to(ROOT)), 'questao': index}
            right = correct[0]
            statement = (str(question.get('textoBase', '')) + '\n' + str(question.get('statement', ''))).strip()
            exp = str(question.get('explanation', ''))
            if len(options) == 2:
                match = re.match(r'^\s*(Certo|Correto|Errado|Incorreto|Verdadeiro|Falso)\b', exp.lstrip('*_ \n'), re.I)
                word = norm(match[1]) if match else None
                expected = 'certo' if word in ['certo', 'correto', 'verdadeiro'] else 'errado'
                if match and expected != norm(right.get('text', '')):
                    findings['prefixo_explicacao_contrario'].append({**loc, 'enunciado': statement, 'marcada': right, 'explicacao': exp})
            else:
                match = letter.search(exp.lstrip('*_ \n'))
                if match and match[1].upper() != str(right.get('key', '')).upper():
                    findings['letra_explicacao_divergente'].append({**loc, 'enunciado': statement, 'marcada': right, 'explicacao': exp})
                numeric = numeric_option(right.get('text', ''))
                if numeric is not None:
                    equal = [o for o in options if o is not right and numeric_option(o.get('text', '')) == numeric]
                    if equal:
                        findings['alternativa_numerica_equivalente_a_correta'].append({**loc, 'enunciado': statement, 'opcoes': options, 'valor': str(numeric)})
            for option in options:
                comment = str(option.get('comentario', ''))
                match = re.match(r'^\s*(Correta|Errada)\s*[:.]', comment, re.I)
                if match and (match[1].lower() == 'correta') != (option.get('correct') is True):
                    findings['comentario_contrario_a_marcacao'].append({**loc, 'enunciado': statement, 'opcao': option})

            texts = [('explanation', exp)] + [('comentario_' + str(o.get('key')), str(o.get('comentario', ''))) for o in options]
            for field, text in texts:
                for match in equation.finditer(text):
                    left, right_expr = match[1].strip(), match[2].strip()
                    if '%' in text[max(0, match.start()-12):match.end()+2]:
                        continue  # Conversão percentual pode mudar a unidade numérica exibida.
                    if not re.search(r'[+*/×·÷^−\-]', left):
                        continue
                    try:
                        actual, claimed = evaluate(left), evaluate(right_expr)
                    except (ValueError, SyntaxError, ZeroDivisionError, OverflowError, TypeError):
                        continue
                    if actual != claimed and abs(actual-claimed) > max(abs(actual)/100, Fraction(1, 1000000)):
                        findings['igualdade_numerica_candidata'].append({**loc, 'campo': field, 'trecho': match[0], 'calculado': str(actual), 'declarado': str(claimed), 'enunciado': statement, 'textoCompleto': text})

            # Mesmo enunciado + mesmo conjunto de opções; compara o TEXTO correto, nunca só letras.
            key = hashlib.sha256(json.dumps([norm(statement), sorted(norm(o.get('text', '')) for o in options)], ensure_ascii=False).encode()).hexdigest()
            answer = norm(correct[0].get('text', ''))
            sighting = {**loc, 'resposta': correct[0].get('text'), 'chave': correct[0].get('key')}
            if key not in fingerprints:
                fingerprints[key] = (answer, sighting)
            elif fingerprints[key][0] != answer:
                if key not in conflicts:
                    conflicts[key] = {'enunciado': statement, 'opcoes': [o.get('text') for o in options], 'locais': [fingerprints[key][1]]}
                conflicts[key]['locais'].append(sighting)
            elif key in conflicts:
                conflicts[key]['locais'].append(sighting)

findings['gabaritos_conflitantes_mesmo_item'] = list(conflicts.values())
report = {'status': 'CANDIDATOS_REQUEREM_CONFIRMACAO; NAO_SAO_TODOS_ERROS', 'baseCommit': 'b13dc28b5', 'metodo': 'Varredura de todos os enunciados, opcoes, explicacoes e comentarios em materias/recortes/simulados. Comparacao textual e numerica restrita, sem chamada a modelos. Igualdades podem ser aproximacoes, erros intencionais dos distratores ou recortes incompletos: ler o contexto antes de classificar.', 'totais': dict(totals), 'contagens': {k: len(v) for k, v in findings.items()}, 'candidatos': dict(findings)}
(OUT / 'varredura-conteudo-candidatos.json').write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n')
print(json.dumps({'totais': report['totais'], 'contagens': report['contagens']}, ensure_ascii=False, indent=2))
for category, items in findings.items():
    print(category, json.dumps(items[:2], ensure_ascii=False)[:2400])
