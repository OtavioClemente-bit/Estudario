# Converter e auditar simulados do ChatGPT (texto da exportação)

Cada arquivo `conteudo/entrada/chatgpt-texto/<id>.txt` é um simulado que o ChatGPT escreveu no chat (markdown com
componentes de interface; às vezes as questões estão num bloco `const sections = [...]`, às vezes em `**Questão N.**`
com alternativas A)–E) e o gabarito/comentários num bloco `answers`/`notes` no fim). Ignore toda a parte de interface.

Para CADA arquivo da sua lista:

1. Leia o arquivo inteiro e TODAS as questões (sem triagem por amostra).
2. Grave `conteudo/entrada/chatgpt/<id>.chatgpt-7.json` (o mesmo `<id>` do .txt), com Write, neste formato exato:

```json
{
  "auditado": true,
  "secoes": [{ "numero": 1, "titulo": "título da seção como está no simulado" }],
  "questoes": [
    { "secao": 1, "enunciado": "...", "gabarito": "Certo", "explicacao": "..." },
    { "secao": 1, "enunciado": "...", "alternativas": ["texto A", "texto B", "texto C", "texto D", "texto E"], "gabarito": "B", "explicacao": "..." }
  ]
}
```

   - Certo/Errado: sem `alternativas`, `gabarito` "Certo" ou "Errado". Múltipla escolha: exatamente 5 alternativas,
     sem a letra no começo do texto, e `gabarito` com a letra.
   - Copie enunciado e alternativas como estão (só corrija erro de digitação ou de conteúdo). Não invente questão.
3. Audite enquanto converte, corrigindo à mão:
   - gabarito errado → corrija; duas alternativas defensáveis → reescreva a errada para ficar errada de verdade;
   - número de lei/artigo/súmula/prazo que você não tem certeza → tire o número e mantenha o conteúdo;
   - ponto controvertido, divergência entre gramáticos, norma que mudou e você não tem certeza → APAGUE a questão;
   - TODA explicação precisa ter 150–300 caracteres explicando o porquê (as do simulado costumam ser curtas:
     reescreva-as mais completas, você mesmo); nunca cite letra ("letra B", "alternativa C") na explicação.
4. Não altere nenhum outro arquivo. Não rode scripts que gerem ou alterem questões (validar o JSON pode).

No fim, responda em até 6 linhas: por arquivo, quantas questões gravou, quantas apagou e quantos gabaritos corrigiu.
