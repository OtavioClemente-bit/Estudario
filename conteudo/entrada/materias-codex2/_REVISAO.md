# Revisão das matérias do Codex (conteudo/entrada/materias-codex2/<id>.json)

Leia AGENTS.md (tudo à mão; nunca invente número; não faça commit). Para cada arquivo seu:
1. FATOS: abra a fonte oficial (as urls em "sources" e o texto compilado no Planalto/órgão). Confira prazos, percentuais,
   composições, datas e artigos citados na teoria, flashcards, resumo e questões. Corrija o que estiver errado ou desatualizado;
   o que não conseguir confirmar, tire o número.
2. QUESTÕES: reescreva as alternativas erradas absurdas ou óbvias (ex.: "Divulgar sua senha", "Custear toda a unidade") por
   distratores plausíveis, que um candidato que não estudou marcaria (troca de prazo, de órgão, de exceção, regra de outra lei).
   Em Certo/Errado, o item errado deve ter um erro sutil e de prova. Confira cada gabarito. Explicação: 150–400 caracteres,
   dizendo o porquê, sem citar letra.
3. TEORIA: garanta em cada capítulo pelo menos uma tabela comparativa quando houver institutos parecidos, "**Pegadinha:**" e
   "**Resumo para a prova**" ao fim. Não aumente além de 30.000 caracteres no total.
4. Valide: deno run --allow-read --allow-env scripts/biblioteca/conferir-materia.ts <arquivo> → OK. Edite com Edit (nunca
   PowerShell Set-Content).
Relatório: por arquivo, erros factuais corrigidos (antes → depois, com a fonte), nº de questões reescritas.

## Atenção extra nesta rodada
- Matéria baseada numa norma específica: se o TEXTO da própria norma não estiver entre as fontes e você não conseguir abri-lo
  (Planalto, Diário Oficial, site do órgão), tudo que depende dela (composição, prazos, artigos) precisa ser confirmado em outra
  fonte oficial ou CORTADO. Se sobrar pouco, diga no relatório que a matéria não deve ser publicada.
- Matérias "guarda-chuva" (português, raciocínio lógico, informática, pedagogia): confira se cobrem o tópico de edital inteiro
  e se as contas/gabaritos de lógica e probabilidade estão certos (refaça cada conta).
