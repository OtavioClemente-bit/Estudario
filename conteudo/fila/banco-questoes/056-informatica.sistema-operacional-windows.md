Você é autor de questões de concurso público no Brasil. Crie um BANCO DE QUESTÕES de **Noções de Informática: Sistema Operacional Windows 10** para o aplicativo Estudário, no nível das provas de tribunais, polícias, agências e fisco. Pesquise na web e use fontes oficiais.

São 2 lotes de 50 questões, um por resposta. Quando eu pedir "lote 1", entregue o lote 1; quando eu pedir "lote 2", entregue o lote 2, sem repetir casos do lote 1.

## Escopo
Uso do Windows 10 em estação de trabalho: interface, janelas, Explorador de Arquivos, caminhos, busca, operações com arquivos e pastas, configurações usuais, extensões, compartilhamento e permissões.
Fica de fora (outras matérias tratam): Administração avançada de domínio, comandos de terminal, registro do Windows, políticas corporativas e recursos exclusivos do Windows 11.

## Tópicos dos editais (distribua as questões entre eles; pelo menos 2 por tópico, mais nos primeiros, que são os mais cobrados; se algum item for claramente de outra matéria, ignore-o)
- Conceitos de organização e de gerenciamento de informações, arquivos, pastas e programas.
- Sistema Operacional Windows 10: manipulação de arquivos e pastas, configurações, permissões etc.
- Sistema operacional Windows.
- Noções de sistema operacional (ambiente Windows).
- Organização e gerenciamento de informações, arquivos, pastas e programas.
- Sistema operacional e ambiente Windows.
- Extensões e arquivos.
- Sistemas Operacionais Windows/Linux: conceito de pastas, diretórios, arquivos e atalhos.
- Área de trabalho e área de transferência.
- Manipulação de arquivos e pastas.
- Organização de informações, arquivos, pastas e programas.
- Organização de arquivos, pastas e programas.
- Noções de sistema operacional Windows.
- Noções de organização e de gerenciamento de informações, arquivos, pastas e programas.
- Identificação e manipulação de arquivos.
- Windows 10: janelas, menus, barra de tarefas, área de trabalho e gerenciamento de arquivos e pastas.
- Compartilhamento, área de transferência, configurações de tela, cores, fontes e impressoras; Windows Explorer.
- Sistema operacional Windows 10.
- Gerenciamento de arquivos, pastas e programas.
- Noções de sistema operacional no ambiente Windows.
- Identificação e manipulação de arquivos e backup.
- Noções de sistema operacional (Windows e Linux).
- Sistema Operacional: Windows/Linux: conceito de pastas, diretórios, arquivos e atalhos, área de trabalho, área de transferência, manipulação de arquivos e pastas, uso dos menus, programas e aplicativos, interação com o conjunto de aplicativos.
- Noções de sistema operacional (Linux e Windows).
- MS-Windows 10: arquivos, pastas, atalhos, área de trabalho, menus, programas e aplicativos.
- MS-Windows 10: pastas, diretórios, arquivos, atalhos, áreas de trabalho e transferência, manipulação de arquivos, menus, programas e aplicativos.
- Conceitos básicos do sistema operacional Windows.
- Principais aplicativos e acessórios do Windows 10.
- Conceitos de organização de pastas e arquivos.
- Principais extensões de arquivos.

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

## Formato de entrega (um bloco JSON válido; se puder, como arquivo informatica.sistema-operacional-windows.banco-N.json, onde N é o lote)
```json
{
  "materia": "informatica.sistema-operacional-windows",
  "lote": 1,
  "questions": [
    { "topico": "<texto exato de um tópico da lista>", "statement": "...", "format": "MULTIPLE_CHOICE", "difficulty": "FACIL",
      "options": [ { "key": "A", "text": "...", "correct": false }, { "key": "B", "text": "...", "correct": true } ],
      "explanation": "..." }
  ]
}
```
- format: "MULTIPLE_CHOICE" (A–E com 5 opções ou A–D com 4 opções) ou "TRUE_FALSE" (opções { "key":"C","text":"Certo" } e { "key":"E","text":"Errado" }). difficulty: "FACIL", "MEDIA" ou "DIFICIL".
