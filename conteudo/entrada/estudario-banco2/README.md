# estudario-banco2

Repositório de trabalho para gerar lotes do banco de questões do Estudário.

- `prompt-opus-091-099.md` e `prompt-opus-122-150.md`: regras + fichas das matérias.
- Cada lote pronto vai na raiz como `<id-da-materia>.banco2-<L>.json`.
- Não colocar código do app aqui.

Mensagem para o Claude Code:

> Leia o arquivo prompt-opus-091-099.md e siga as regras dele. Escreva TODOS os lotes de todas as matérias, um por um, sem parar e sem me perguntar nada. Cada lote vai num arquivo <id>.banco2-<L>.json na raiz, gravado inteiro só quando estiver pronto (pode escrever em duas metades de 25 e juntar). Antes de gravar, conte: 50 questões; cada letra correta 8 a 12 vezes; correta mais longa no máximo 16 e mais curta no máximo 16. Só use código para conferir os números e validar o JSON, nunca para gerar ou alterar questões. Faça commit e push de cada lote ao terminar e passe direto para o próximo. Pare só quando acabar tudo.
