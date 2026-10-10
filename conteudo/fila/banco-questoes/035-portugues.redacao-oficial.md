Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Redação oficial** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Características da redação oficial, clareza, concisão, impessoalidade, formalidade, padrão ofício, organização do ofício, fechos, endereçamento, vocativo e emprego de pronomes de tratamento conforme o Manual de Redação da Presidência da República.
Fica de fora (outras matérias tratam): Elaboração de atos normativos, legislação administrativa específica e modelos próprios de órgãos ou entidades.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Redação Oficial.
- Correspondência oficial (conforme Manual de Redação da Presidência da República).
- Correspondência oficial (conforme Manual de Redação da Presidência da República): adequação da linguagem ao tipo de documento.
- Aspectos gerais da redação oficial.
- Redação de correspondências oficiais.
- Correspondência oficial: adequação do formato do texto ao gênero.
- Correspondência oficial, conforme o Manual de Redação da Presidência da República: adequação da linguagem ao tipo de documento.
- Correspondência oficial, conforme o Manual de Redação da Presidência da República: adequação do formato do texto ao gênero.
- Correspondência oficial: adequação da linguagem ao tipo de documento.
- Correspondência oficial (conforme Manual de Redação da Presidência da República): Adequação do formato do texto ao gênero.
- Finalidade dos expedientes oficiais.
- Redação de correspondências oficiais (Manual de Redação da Presidência da República).
- Redação de correspondências oficiais: adequação da linguagem ao tipo de documento.
- Redação de correspondências oficiais: adequação do formato do texto ao gênero.
- Adequação da linguagem ao tipo de documento.
- Redação oficial: tipos de documentos oficiais.
- Aspectos gerais da redação oficial: Finalidade dos expedientes oficiais.
- Aspectos gerais da redação oficial: Adequação da linguagem ao tipo de documento.
- Aspectos gerais da redação oficial: Adequação do formato do texto ao gênero.
- Aspectos gerais da redação oficial: Pronomes de tratamento.
- Redação Oficial: Manual de Redação da Presidência da República.
- Redação oficial segundo o Manual de Redação da Presidência da República e Decreto nº 9.758/2019.
- Redação Oficial (conforme o Manual de Redação Oficial da Presidência da República): uso da norma culta da linguagem, clareza e precisão, objetividade, concisão, coesão e coerência, impessoalidade, formalidade e padronização.
- Redação oficial conforme o Manual de Redação da Presidência da República: norma culta, clareza, precisão, objetividade, concisão, coesão, coerência, impessoalidade, formalidade e padronização.
- Decreto nº 9.758/2019: tratamento e endereçamento nas comunicações com agentes públicos federais.
- Redação oficial: escrita de textos formais e Manual de Redação da Presidência da República (disponível no sítio do Planalto, na internet).
- Correspondência oficial.
- Manual de Redação da Presidência da República.
- Redação Oficial (conforme Manual de Redação da Presidência da República): aspectos gerais, gêneros textuais, finalidade dos principais modelos.
- Correspondência oficial: linguagem e formato conforme o gênero documental.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.redacao-oficial.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.redacao-oficial",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
