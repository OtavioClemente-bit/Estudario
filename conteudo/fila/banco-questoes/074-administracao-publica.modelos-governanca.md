Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Administração Pública: Modelos de administração pública e governança** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Patrimonialismo, burocracia weberiana e suas disfunções, administração pública gerencial (nova gestão pública), reformas do Estado no Brasil (reforma burocrática, Decreto-Lei 200/1967, Plano Diretor de 1995 e Emenda 19/1998), governança pública e seus princípios e mecanismos, accountability (vertical, horizontal e social), gestão por resultados e indicadores de desempenho, excelência nos serviços públicos e pós-gerencialismo.
Fica de fora (outras matérias tratam): Princípios e regras detalhadas do Direito Administrativo, licitações e contratos, Lei de Acesso à Informação, atendimento ao público, planejamento e orçamento (PPA, LDO, LOA) e gestão de projetos e da qualidade em geral, que estão em outras matérias.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Gestão de resultados na produção de serviços públicos.
- Convergências e diferenças entre a gestão pública e a gestão privada.
- Empreendedorismo governamental e novas lideranças no setor público.
- O paradigma do cliente na gestão pública.
- Gestão da Qualidade: excelência nos serviços públicos.
- A nova gestão pública.
- Governabilidade, governança e accountability.
- Governança e gestão pública.
- Administração pública: do modelo racional-legal ao paradigma pós-burocrático.
- Gestão por resultados na produção de serviços públicos.
- Reformas administrativas.
- Evolução da administração pública no Brasil (após 1930).
- Evolução da Administração Pública brasileira e reformas administrativas: princípios, objetivos, resultados, patrimonialismo, burocracia e gerencialismo.
- Estruturação da máquina administrativa no Brasil desde 1930: dimensões estruturais e culturais.
- Governança pública.
- Evolução dos modelos da administração pública (patrimonialista, burocrática e gerencial).
- Modelos de gestão pública (patrimonialista, burocrática e gerencial), com destaque para a Reforma do Estado e a Nova Gestão Pública.
- Gestão pública contemporânea, abordando temas como governança, accountability, transparência, participação social, planejamento governamental (PPA, LDO e LOA), gestão por resultados e indicadores de desempenho.
- Accountability.
- Evolução dos modelos de gestão pública: patrimonialismo, administração burocrática e nova gestão pública ( New Public Management ).
- Nova Governança Pública ( New Public Governance ): conceito, fundamentos e evolução.
- Ação pública em redes de cooperação, articulação interinstitucional e parcerias com o setor privado e terceiro setor.
- Valor Público, cadeia de valor público.
- Governança na Administração Pública.
- Governança pública: conceito, distinção entre governança e governabilidade e alinhamento estratégico.
- Governança na Administração Pública: mecanismos de governança: liderança, estratégia e controle.
- Conceito de valor público: geração de valor para a sociedade, entrega de resultados, sustentabilidade e avaliação de impactos de políticas públicas.
- Decreto Federal nº 9.203/2017 e suas alterações (política de governança da administração pública federal direta, autárquica e fundacional).
- Governabilidade e governança; intermediação de interesses: clientelismo, corporativismo e neocorporativismo.
- Evolução da Administração Pública no Brasil após 1930, reformas administrativas e nova gestão pública.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo administracao-publica.modelos-governanca.banco-N.json, onde N é o lote)
```json
{
  "materia": "administracao-publica.modelos-governanca",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false, "comentario": "erro específico desta alternativa" }, { "key": "B", "text": "...", "correct": true, "comentario": "por que está certa" } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (sempre 5 opções A–E) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
