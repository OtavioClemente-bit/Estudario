Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Geral: Comportamento organizacional: motivação, liderança, grupos, conflito, poder e mudança** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Conceito e níveis do comportamento organizacional; motivação (intensidade, direção e persistência, intrínseca e extrínseca), teorias de conteúdo e de processo em profundidade: ERG de Alderfer, necessidades adquiridas de McClelland, expectativa de Vroom, equidade de Adams e justiça organizacional, fixação de objetivos de Locke, reforço de Skinner, modelo das características do trabalho, enriquecimento e ampliação de cargos; liderança: traços, estudos de Ohio e Michigan, grade gerencial, Fiedler, caminho-meta, situacional (comparação), LMX, transacional, transformacional, carismática, servidora e substitutos da liderança; grupos e equipes: tipos, estágios de Tuckman, equilíbrio pontuado, papéis, normas, coesão, folga social, pensamento grupal, polarização, grupo x equipe; conflito: visões, tipos, processo, estilos de Thomas e Kilmann, negociação distributiva e integrativa, MAANA; poder: dependência, bases de French e Raven, tipologia de Etzioni, táticas de influência, política organizacional e empowerment; mudança: forças, agentes, tipos, Lewin e campo de forças, oito etapas de Kotter, resistência e táticas de Kotter e Schlesinger, desenvolvimento organizacional, aprendizagem de circuito simples e duplo e organizações que aprendem.
Fica de fora (outras matérias tratam): Teorias da administração, funções PODC, comunicação, controle, tomada de decisão e cultura organizacional (matéria de funções administrativas); subsistemas de gestão de pessoas, clima organizacional e qualidade de vida no trabalho (matéria de gestão de pessoas); Maslow, Herzberg e McGregor aparecem aqui só como comparação; mediação e conciliação judiciais; psicologia clínica.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Comportamento organizacional.
- Motivação.
- Comportamento organizacional: relações indivíduo/organização.
- Comportamento organizacional: Liderança, motivação e desempenho.
- Liderança.
- Gestão de conflitos.
- Gestão da mudança.
- Comportamento organizacional: relações indivíduo/organização, motivação, liderança, desempenho.
- Atitudes e satisfação no trabalho.
- Direção: motivação e liderança.
- Gerenciamento de conflitos.
- Trabalho em equipe.
- Motivação e liderança.
- Trabalho em equipe: relacionamento interpessoal e empatia.
- Comportamento organizacional e relações indivíduo-organização.
- Liderança, motivação, desempenho e qualidade de vida.
- Gestão de processos de mudança organizacional: Conceito de mudança.
- Mudança e inovação organizacional.
- Grupos e equipes de trabalho.
- Liderança; Estilos de liderança e situações de trabalho.
- Teorias da motivação.
- Liderança, autoliderança e liderança de equipes.
- Aspectos comportamentais da organização: liderança, motivação, comunicação e desempenho.
- Competência interpessoal e gerenciamento de conflitos.
- Liderança e motivação.
- O indivíduo e o contexto organizacional: variáveis individuais, grupais e organizacionais.
- O indivíduo e o contexto organizacional: variáveis individuais, grupais e organizacionais: Comportamento humano no trabalho: satisfação e comprometimento.
- O indivíduo e o contexto organizacional: variáveis individuais, grupais e organizacionais: Equipes e grupos de trabalho.
- O indivíduo e o contexto organizacional: variáveis individuais, grupais e organizacionais: Competência interpessoal.
- Equipes de trabalho.

## Cada lote de 50
- 38 de múltipla escolha A a E (5 alternativas) e 12 de Certo/Errado. (O app transforma A–E em A–D tirando uma errada, por isso cada alternativa tem comentário próprio.)
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Em múltipla escolha: cada alternativa tem "comentario" próprio (1–2 frases: por que está certa ou, se errada, qual o erro específico) e a "explanation" geral (mínimo 80 caracteres) explica a regra/conceito **sem citar letras** (nada de "A", "B", "alternativa C"). Em Certo/Errado a explanation começa com "Certo." ou "Errado." e explica o item (mínimo 120 caracteres). Cada texto escrito de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo administracao-geral.comportamento-organizacional.banco-N.json, onde N é o lote)
```json
{
  "materia": "administracao-geral.comportamento-organizacional",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
