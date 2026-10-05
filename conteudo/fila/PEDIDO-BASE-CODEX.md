# Pedido para o Codex — matérias de base (as que quase todo concurso cobra)

Regras: seguir o AGENTS.md (escrever tudo à mão, sem script; gravar só em conteudo/entrada/<id>.json,
arquivo inteiro de uma vez). Matéria NEUTRA desde o início: sem banca (FUMARC, CEBRASPE, FGV...), sem órgão
(TRT, PM, bombeiros) como contexto, exemplos de escritório, repartição, tribunal e dia a dia. Capítulo
"Como o tema costuma ser cobrado". board e agency = null. Mínimo do conferidor: 40+ questões, 15+ de
múltipla escolha, 8+ difíceis, gabarito espalhado de A a E, a certa não pode ser sempre a mais longa,
gabarito igual à explicação. Validar com validateLibraryMaterial (supabase/functions/_shared/library.ts).
Uma matéria por arquivo, version 1, status igual às outras matérias publicadas.

Estas NÃO existem ainda na biblioteca e não estão nos lotes do TRT (não repetir as que já existem).

## Português (pasta portugues)
- classes-de-palavras — substantivo, adjetivo, advérbio, preposição, conjunção, interjeição
- termos-da-oracao — sujeito, predicado, objetos, complemento nominal, adjunto, aposto, vocativo
- vozes-verbais — ativa, passiva analítica e sintética, reflexiva; transposição
- funcoes-que-se — usos de "que" e "se"
- formacao-de-palavras — derivação, composição, hibridismo, onomatopeia
- figuras-de-linguagem — metáfora, metonímia, ironia, eufemismo, hipérbole etc.
- tipologia-textual — narração, descrição, dissertação, injunção, exposição
- redacao-oficial — Manual de Redação da Presidência da República: ofício, padrão ofício, pronomes de tratamento

## Matemática e raciocínio lógico (pasta matematica)
- razao-proporcao
- mmc-mdc-divisibilidade
- equacoes-1-2-grau
- sistemas-lineares
- juros-simples-compostos
- analise-combinatoria
- probabilidade
- progressoes-pa-pg
- geometria-plana — áreas, perímetros, Pitágoras
- sequencias-logicas — números, letras e figuras
- equivalencias-negacoes-logicas — negação de "e", "ou", "se...então", leis de De Morgan
- argumentacao-logica — validade de argumentos, silogismos
- quantificadores-diagramas — todo, algum, nenhum, diagramas de Venn

## Informática (pasta informatica)
- hardware-conceitos-basicos — componentes, memória, armazenamento, periféricos
- internet-navegadores — Chrome, Edge, Firefox, abas, histórico, navegação anônima, URL
- computacao-nuvem — armazenamento, compartilhamento, sincronização, backup
- windows-10-11 — gerenciador de arquivos, atalhos, configurações (complementa a de Windows que já existe)

## Direito penal e processo penal (pasta direito-penal e processo-penal) — cai em polícia, tribunais e outros
- direito-penal/aplicacao-lei-penal — tempo, lugar, princípios
- direito-penal/teoria-do-crime — fato típico, ilicitude, culpabilidade, tentativa, consumação
- direito-penal/imputabilidade-concurso-pessoas
- direito-penal/penas — espécies, aplicação, extinção da punibilidade
- direito-penal/crimes-contra-pessoa
- direito-penal/crimes-contra-patrimonio
- processo-penal/inquerito-policial
- processo-penal/acao-penal
- processo-penal/prisoes-liberdade-provisoria

## Gestão e serviço público (pasta administracao-publica)
- lei-acesso-informacao — Lei 12.527/2011
- atendimento-ao-publico — qualidade no atendimento, comunicação, postura
- nocoes-arquivologia — gestão de documentos, protocolo, classificação, temporalidade, arquivos corrente/intermediário/permanente

Ordem sugerida: português e matemática primeiro (caem em tudo), depois informática, depois o resto.
