# Auditar arquivos de questões do ChatGPT (conteudo/entrada/chatgpt/*.chatgpt-N.json)

Para CADA arquivo da sua lista, leia TODAS as questões (sem triagem por amostra) e corrija à mão, editando o próprio
arquivo (Edit), sem mudar o formato/estrutura dele:

- gabarito errado → corrija; duas alternativas defensáveis → reescreva a errada para ficar errada de verdade;
- número de lei/artigo/súmula/prazo de que você não tem certeza → tire o número e mantenha o conteúdo;
- ponto controvertido, divergência entre gramáticos, norma recente que você não conhece, pena de que não tem
  certeza → APAGUE a questão (não "corrija" para o que você acha);
- toda explicação com menos de 140 caracteres → reescreva com 150–300 explicando o porquê;
- explicação que cita letra ("letra B", "alternativa C") → reescreva sem citar letra;
- NUNCA invente alternativa nem complete questão que veio com menos de 5 alternativas (deixe como está).

Por último, acrescente `"auditado": true,` logo depois da primeira `{` do arquivo. Confira que o JSON abre
(`deno eval` com JSON.parse pode). Não altere nenhum outro arquivo.

No fim, responda em até 6 linhas: por arquivo, quantas questões apagou e quantos gabaritos corrigiu.
