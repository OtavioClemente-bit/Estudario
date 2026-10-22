"""Verifica contas e contraexemplos da revisão; não gera nem altera questões."""
from fractions import Fraction as F
from pathlib import Path
import json

resultados = []


def conferir(nome, calculado, esperado):
    assert calculado == esperado, (nome, calculado, esperado)
    resultados.append({"verificacao": nome, "resultado": str(calculado), "passou": True})


# Probabilidade 88: as duas condicionais podem ser zero com marginais diferentes.
universo = set(range(1, 5))
a, b = {1}, {2, 3}
conferir("q88 P(A)", F(len(a), len(universo)), F(1, 4))
conferir("q88 P(B)", F(len(b), len(universo)), F(1, 2))
conferir("q88 P(A|B)", F(len(a & b), len(b)), F(0))
conferir("q88 P(B|A)", F(len(a & b), len(a)), F(0))

# Probabilidade 139: três modelos atendem às mesmas quatro probabilidades fornecidas.
universo = set(range(1, 101))
a1 = set(range(1, 31))
a2 = set(range(1, 11)) | set(range(31, 91))
base_b = {1, 2, 3, 11, 12, 13, 31, 32, 33, 34}
modelos = []
for fora in [2, 3, 5]:
    evento_b = base_b | set(range(91, 91 + fora))
    assert F(len(a1), 100) == F(3, 10)
    assert F(len(a2), 100) == F(7, 10)
    assert F(len(evento_b & a1), len(a1)) == F(1, 5)
    assert F(len(evento_b & a2), len(a2)) == F(1, 10)
    esperado = {2: F(12, 100), 3: F(13, 100), 5: F(15, 100)}[fora]
    conferir(f"q139 P(B) com {fora} resultados de B fora de A1 uniao A2", F(len(evento_b), 100), esperado)
    modelos.append({"B_fora": fora, "P_B": str(esperado), "A1_intersecao_A2": len(a1 & a2), "fora_da_uniao": len(universo - (a1 | a2))})

conferir("probabilidade q125 uma ordem", F(3, 8) * F(5, 7), F(15, 56))
conferir("probabilidade q125 duas ordens sem reposicao", 2 * F(3, 8) * F(5, 7), F(15, 28))
conferir("probabilidade q125 duas ordens com reposicao", 2 * F(3, 8) * F(5, 8), F(15, 32))
conferir("probabilidade q145 Bayes", (F(3, 10) * F(1, 5)) / (F(3, 10) * F(1, 5) + F(7, 10) * F(1, 20)), F(12, 19))
conferir("porcentagem q49 recuperacao do total", F(72) / F(24, 100), F(300))
conferir("porcentagem q79 erro de taxas na mesma base", F(500) * (1 + F(12, 100) - F(10, 100)), F(510))
conferir("porcentagem q79 calculo correto", F(500) * F(112, 100) * F(9, 10), F(504))
conferir("porcentagem q99 somente dois aumentos", (F(11, 10) ** 2 - 1) * 100, F(21))
conferir("porcentagem q99 tres aumentos", (F(11, 10) ** 3 - 1) * 100, F(331, 10))
conferir("porcentagem q104 ignorar desconto final", (F(6, 5) ** 2 - 1) * 100, F(44))
conferir("porcentagem q104 calculo correto", (F(6, 5) ** 2 * F(4, 5) - 1) * 100, F(76, 5))
conferir("porcentagem q112 igualdade no limite de 100%", F(1) * F(1, 5), F(1, 5))
conferir("porcentagem q124 propriedade dentro do grupo", F(400) * F(1, 2) * F(3, 10), F(60))
conferir("porcentagem q124 total admissivel com mais 20 fora do grupo", F(60 + 20), F(80))

report = {"metodo": "Fracoes exatas e enumeracao de conjuntos finitos; valida somente os casos registrados, nao todo o banco automaticamente.", "verificacoes": len(resultados), "resultados": resultados, "modelosQ139": modelos}
Path(__file__).with_name("matematica-contraexemplos.json").write_text(json.dumps(report, ensure_ascii=False, indent=2) + "\n")
print(f"{len(resultados)} verificacoes exatas aprovadas; tres modelos distintos para probabilidade 139.")
