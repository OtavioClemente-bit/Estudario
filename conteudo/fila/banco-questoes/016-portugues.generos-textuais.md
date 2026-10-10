Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Gêneros textuais e tipos textuais** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Distinção entre gênero e tipo textual; finalidade, interlocutores, domínio discursivo, suporte, circulação, composição, estilo, marcas linguísticas, hibridismo e reconhecimento contextual de gêneros frequentes.
Fica de fora (outras matérias tratam): Interpretação aprofundada de textos sem foco em reconhecimento de gênero ou tipo; regras ortográficas e análise morfossintática fora das pistas necessárias à classificação.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Reconhecimento de tipos e gêneros textuais.
- Adequação do formato do texto ao gênero.
- Tipologia e gêneros textuais.
- Tipos textuais: informativo, publicitário, propagandístico, normativo, didático e divinatório.
- Textos literários e não literários.
- Elementos de construção do texto e seu sentido: gênero do texto (literário e não literário, narrativo, descritivo e argumentativo).
- Tipos e gêneros textuais, documentos oficiais, organização, funções, características linguísticas e propósito comunicativo.
- Tipos textuais informativo, publicitário, propagandístico, normativo, didático e divinatório.
- Compreensão e interpretação de textos de gêneros variados : Características dos diversos gêneros textuais.
- Tipos e gêneros textuais, ortografia e coesão.
- Gêneros e tipos textuais, intertextualidade, características e estrutura.
- Reconhecimento de tipos e gêneros textuais e ortografia oficial.
- Reconhecimento de tipos e gêneros textuais e domínio da ortografia oficial.
- Gêneros e tipos textuais e intertextualidade: características e estrutura.
- Principais tipos e gêneros textuais e suas funções.
- Características e funcionalidades de gêneros textuais variados.
- Tipos e gêneros textuais.
- Elementos de construção do texto e seu sentido: gêneros literários e não literários, narrativos, descritivos e argumentativos.
- Gêneros e tipologias textuais.
- Modos de organização discursiva e tipos textuais informativo, publicitário, propagandístico, normativo, didático e divinatório.
- Tipos textuais informativo, publicitário, propagandístico, normativo, didático e divinatório e textos literários e não literários.
- Papéis sociais e comunicativos dos interlocutores, relação entre usos e propósitos comunicativos.
- Função sócio-comunicativa do gênero.
- Estudo dos gêneros digitais.
- Gêneros discursivos e sequências textuais.
- Gêneros e estilos textuais.
- Gêneros textuais, interpretação, organização interna, semântica e emprego vocabular.
- Gêneros e tipos textuais: classificação dos gêneros textuais narrativo, descritivo, dissertativo, injuntivo, expositivo e dialogal.
- Características estruturais, funcionais e linguísticas dos diferentes gêneros.
- Hibridismo e multifuncionalidade textual.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.generos-textuais.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.generos-textuais",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
