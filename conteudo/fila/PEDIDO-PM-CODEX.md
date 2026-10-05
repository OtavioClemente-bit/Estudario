# Pedido para o Codex — matérias comuns aos concursos de Polícia Militar (Soldado)

Regras: seguir o AGENTS.md (escrever tudo à mão, sem script; gravar só em conteudo/entrada/<nome>.json, arquivo
inteiro, UTF-8 SEM BOM). Matéria NEUTRA: sem banca, sem órgão/estado específico como contexto do aluno; exemplos
de rua, delegacia, fórum, escritório e dia a dia. Capítulo "Como o tema costuma ser cobrado". board e agency = null.
status "PUBLISHED", version 1, theoryTitle preenchido. Se não tiver certeza de um número de artigo, prazo ou pena,
explique o conteúdo sem citar o número (nunca invente norma; use a redação vigente).

Formato que passa no conferidor: 40 a 50 questões, cerca de 25 de múltipla escolha (A–E) e 20 de Certo/Errado
(TRUE_FALSE, opções C=Certo e E=Errado, explicação começando por "Certo." ou "Errado." e sem falar em
alternativas); pelo menos 8 FÁCIL, 8 MÉDIA e 8 DIFÍCIL; teoria somando 6.000+ caracteres; explicações com 120+
caracteres; gabarito espalhado entre A e E; a certa não pode ser a mais longa em mais de 40%; uma só correta.
Se uma explicação disser que há duas respostas, conserte a questão. Confira se a letra citada na explicação é a
marcada. Modelo pronto: conteudo/materias/direito-penal/teoria-do-crime.json.

COMO TRABALHAR: uma matéria por vez; crie o JSON com a ferramenta de editar arquivos (não por comando longo do
PowerShell); rascunho fora de conteudo/, rode validateLibraryMaterial, CORRIJA (não descarte) e grave. Siga até o
fim sem pedir confirmação. Antes de cada uma, veja se há matéria parecida em conteudo/materias e não repita
apelidos dela.

## Bloco 1 — Penal e processual militar
- direito-penal-militar.parte-geral — Código Penal Militar, parte geral: crime militar (tempo de paz), aplicação da lei penal militar, imputabilidade, excludentes, penas principais e acessórias.
- direito-penal-militar.crimes-militares-especie — crimes militares mais cobrados: contra a autoridade ou disciplina (motim, revolta, desrespeito, insubordinação), contra o serviço e o dever militar (deserção, abandono de posto, embriaguez em serviço), peculato e concussão militares.
- processo-penal-militar.processo-penal-militar — Código de Processo Penal Militar: polícia judiciária militar, ação penal militar, competência da Justiça Militar estadual (inclusive a do júri para crime doloso contra a vida de civil), prisões e menagem.
- processo-penal-militar.inquerito-policial-militar — IPM: instauração, encarregado, prazos, diligências, relatório e arquivamento; auto de prisão em flagrante militar.

## Bloco 2 — Legislação penal especial I
- legislacao-penal-especial.lei-drogas — Lei 11.343/2006: usuário x traficante, tráfico privilegiado, associação, procedimento e medidas.
- legislacao-penal-especial.estatuto-desarmamento — Lei 10.826/2003: posse x porte, registro, crimes (posse irregular, porte ilegal, disparo, comércio ilegal, tráfico internacional).
- legislacao-penal-especial.maria-da-penha — Lei 11.340/2006: formas de violência, medidas protetivas, descumprimento, atuação policial e prisão.
- legislacao-penal-especial.crimes-hediondos-tortura — Lei 8.072/1990 (rol, consequências) e Lei 9.455/1997 (tortura: modalidades, omissão, efeitos).

## Bloco 3 — Legislação penal especial II
- legislacao-penal-especial.eca-ato-infracional — ECA: ato infracional, apreensão, garantias, medidas socioeducativas e de proteção, crimes contra criança e adolescente mais cobrados.
- legislacao-penal-especial.crimes-transito — crimes do CTB: homicídio e lesão culposa na direção, embriaguez ao volante, racha, fuga do local, direção sem habilitação.
- legislacao-penal-especial.juizados-especiais-criminais — Lei 9.099/1995: infração de menor potencial ofensivo, termo circunstanciado, composição civil, transação penal, suspensão condicional do processo.
- direito-penal.crimes-dignidade-sexual — estupro, estupro de vulnerável, importunação sexual, assédio sexual, registro não autorizado de intimidade sexual.
- legislacao-penal-especial.organizacoes-criminosas — Lei 12.850/2013: conceito, colaboração premiada, ação controlada, infiltração.

## Bloco 4 — Segurança pública
- seguranca-publica.seguranca-publica-constituicao — art. 144 da Constituição: órgãos, atribuições da PM e do Corpo de Bombeiros, polícia judiciária, guardas municipais, segurança viária.
- seguranca-publica.susp-lei-13675 — Lei 13.675/2018 (SUSP): princípios, diretrizes, integrantes, PNSPDS e conselhos.
- seguranca-publica.uso-forca-direitos-humanos — uso diferenciado e progressivo da força, princípios (legalidade, necessidade, proporcionalidade), armas de menor potencial ofensivo e a Lei 13.060/2014, direitos do preso e da pessoa abordada.
- seguranca-publica.criminologia — conceito, objeto (crime, criminoso, vítima, controle social), escolas, vitimologia, prevenção primária/secundária/terciária.

## Bloco 5 — Gerais de PM
- lingua-inglesa.interpretacao-texto-ingles — leitura e interpretação de textos curtos em inglês, vocabulário em contexto, falsos cognatos, tempos verbais e conectivos mais cobrados.
- legislacao.estatuto-pessoa-idosa — Lei 10.741/2003: direitos, medidas de proteção e crimes mais cobrados.
- legislacao.igualdade-racial-crimes-preconceito — Estatuto da Igualdade Racial e Lei 7.716/1989 (crimes de preconceito), injúria racial.
- processo-penal.busca-pessoal-abordagem — busca pessoal e domiciliar, fundada suspeita, flagrante, uso de algemas, abordagem e garantias.
