Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Informática: Gestão de segurança da informação: ISO/IEC 27001 e 27002** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Pilares da segurança da informação (confidencialidade, integridade, disponibilidade); família ISO/IEC 27000; sistema de gestão de segurança da informação (SGSI); estrutura da ISO/IEC 27001:2022 (cláusulas 4 a 10, avaliação e tratamento de riscos, Declaração de Aplicabilidade, auditoria interna, análise crítica, melhoria contínua e certificação); ISO/IEC 27002:2022 (93 controles em 4 temas, atributos, controles novos) e diferenças em relação à versão de 2013; gestão de incidentes de segurança (evento, incidente, resposta, lições aprendidas, evidências); gestão de continuidade de negócios (análise de impacto, RTO, RPO, plano de continuidade, prontidão de TIC).
Fica de fora (outras matérias tratam): Tipos de malware e golpes (outra matéria), criptografia em detalhe, configuração de firewall e ferramentas, ISO/IEC 27005 e ISO 31000 em profundidade, LGPD, normas complementares do governo federal e frameworks como NIST CSF, CIS Controls e COBIT.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Segurança da informação.
- Gestão de segurança da informação.
- Gestão de Segurança da Informação e Privacidade.
- Conhecimentos em estruturação da gestão de segurança da informação, elaboração de Políticas e Normas de segurança, e acompanhamento do desempenho.
- Gestão de riscos da segurança da Informação.
- Conhecimentos na estruturação da disciplina de Gestão de Riscos de SI, e na condução de Análises de Riscos da SI.
- Tecnologia da informação e segurança da informação.
- Tecnologia da informação e segurança de dados.
- Políticas de segurança da informação.
- Referências principais: ISO 31000, ISO 31010, ISSO 27005 (em suas versões mais recentes).
- Planejamento, identificação e análise de riscos.
- Plano de continuidade de negócio.
- Classificação e controle de ativos de informação, segurança de ambientes físicos e lógicos, controles de acesso.
- Definição, implantação e gestão de políticas de segurança e auditoria.
- Prevenção e tratamento de incidentes.
- Gestão de riscos de segurança da informação.
- Normas ABNT NBR ISO/IEC 27001:2022 e 27002:2022.
- Políticas, procedimentos e gerenciamento da segurança da informação.
- Procedimentos de segurança, conceitos gerais de gerenciamento.
- Noções de segurança da informação, incluindo conceitos de confidencialidade, integridade, disponibilidade e autenticidade.
- Gestão de riscos e continuidade de negócio.
- Gestão de riscos.
- Confiabilidade, integridade e disponibilidade.
- Gestão de segurança da informação: NBR ISO/IEC 27001 e NBR ISO/IEC 27002.
- Noções de segurança da informação.
- Gestão de continuidade do negócio.
- Prevenção e tratamento de incidentes de segurança da informação.
- Segurança da Informação e Proteção de Dados: princípios de confidencialidade, integridade, disponibilidade e rastreabilidade.
- ABNT NBR 27002:2019;
- ABNT NBR 27035-3:2021;

## Cada lote de 50
- 25 de múltipla escolha A a E (5 alternativas), 13 de múltipla escolha A a D (4 alternativas), 12 de Certo/Errado.
- Dificuldade: 15 fáceis, 20 médias, 15 difíceis. Gabarito espalhado entre as letras; Certo/Errado com metade de cada.
- Varie o tipo: caso concreto, comparação entre conceitos parecidos. Use casos DIFERENTES entre si; nada de repetir o mesmo caso com outras palavras.

## Regras de qualidade
1. Regra precisa, com exemplos concretos; nunca invente regra ou dado.
2. **Todo número, nome ou dado usado nas alternativas e na explicação precisa estar no enunciado** (ou ser resultado de conta com os dados do enunciado).
3. Distratores = erro de quem estudou MAL (conceito vizinho, número ou prazo trocado, exceção esquecida, conta com base errada). Proibido distrator absurdo, de outro assunto, "todas/nenhuma das anteriores" ou absolutos só para marcar o errado.
4. Alternativas com tamanho parecido; a certa não pode ser a mais longa em mais de 1/3 das questões.
5. Explicação (mínimo 120 caracteres): por que a certa está certa e por que CADA errada erra, com o conceito específico. Em Certo/Errado começa com "Certo." ou "Errado.". Escreva cada explicação de forma própria: proibido frase-molde repetida entre questões.
6. Texto neutro: não escreva nome de banca, órgão ou cargo, nem a palavra "banca" (use "a prova").
7. Antes de entregar, resolva cada questão como candidato: uma única alternativa defensável e gabarito certo.

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.seguranca-normas-iso27001.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.seguranca-normas-iso27001",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
