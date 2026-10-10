Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Língua Portuguesa: Tipologia textual** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Tipos e sequências textuais narrativas, descritivas, expositivas, explicativas, argumentativas, injuntivas e dialogais; finalidade, organização, marcas linguísticas, predominância e combinação em textos reais, com distinção introdutória entre tipo e gênero e entre literatura e não literatura.
Fica de fora (outras matérias tratam): Classificação aprofundada de gêneros textuais, interpretação global desvinculada da tipologia e análise sintática extensa.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Tipologia textual.
- Modos de organização discursiva: descrição, narração, exposição, argumentação e injunção.
- Os modos de organização discursiva: a descrição, a narração, a exposição informativa e a exposição argumentativa.
- Modos de organização discursiva: descrição, narração, exposição, argumentação e injunção; características específicas de cada modo.
- Tipos textuais: informativo, publicitário, propagandístico, normativo, didático e divinatório; características específicas de cada tipo.
- Organização estrutural dos textos.
- Características específicas de cada modo.
- Características específicas de cada tipo.
- Modos de organização discursiva e tipos textuais.
- Modos de organização discursiva: descrição, narração, exposição informativa e exposição argumentativa.
- Textos literários e não literários e tipologia da frase portuguesa.
- Sequências textuais descritiva, narrativa, argumentativa, injuntiva e dialogal.
- Características específicas de cada tipo textual.
- Características específicas dos modos de organização discursiva.
- Tipos textuais e características dos textos literários e não literários.
- Tipos textuais e suas características; textos literários e não literários.
- Modos discursivos, tipos textuais e textos literários e não literários.
- Tipos textuais, textos literários e não literários e tipologia da frase portuguesa.
- Modos discursivos: descrição, narração, exposição, argumentação e injunção.
- Modos de organização discursiva e tipos textuais previstos no edital.
- Modos discursivos e tipos textuais indicados no edital.
- Tipos textuais, textos literários e não literários.
- Tipos textuais, tipologia e estrutura da frase portuguesa.
- Tipos textuais e textos literários e não literários.
- Modos de organização discursiva: descrição, narração, exposição, argumentação e injunção e suas características.
- Organização retórica: generalização, exemplificação, descrição, definição, exemplificação/especificação, explanação, classificação, elaboração.
- Tipologia textual e gênero textual: narração, descrição, dissertação, carta (argumentativa, familiar, comercial, convite etc.).
- Modos discursivos, tipos textuais, textos literários e não literários e tipologia da frase.
- Modos discursivos, tipos textuais, textos literários e não literários e estrutura da frase.
- Características específicas de cada modo de organização discursiva.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo portugues.tipologia-textual.banco-N.json, onde N é o lote)
```json
{
  "materia": "portugues.tipologia-textual",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
