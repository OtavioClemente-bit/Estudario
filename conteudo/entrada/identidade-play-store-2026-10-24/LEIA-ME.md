# Estudário — nova apresentação na Play Store e ícone
Data: 24/10/2026.

Foram criadas três pinturas com o ImageGen integrado e uma apresentação que mantém a identidade do Estudário: páginas creme, capa e capelo índigo, marcador menta, luz quente, nanquim, guache e lápis de cor. A tipografia Nunito vem do próprio projeto. Textos e telas do aplicativo foram diagramados separadamente das pinturas, para manter a nitidez.

## Arquivos para a loja
- play-store/icone-512.png: ícone de 512 × 512, PNG RGBA com fundo integralmente opaco. Não tem cantos arredondados ou sombra de contorno pré-aplicados; a loja faz esse tratamento.
- play-store/capa-1024x500.png: capa horizontal de 1024 × 500, PNG RGB sem transparência.
- play-store/01-seu-estudo.png a 06-sua-revisao.png: seis peças independentes de 1080 × 1920, PNG RGB sem transparência. Cada peça tem sua composição inteira, sem elementos que dependam da imagem vizinha.

As telas foram capturadas do app real instalado no celular, versão 3.3.116, código 144, com autorização do dono. O início, os concursos, o plano, o tópico, a área de questões e as revisões refletem o estado visto em 24/10/2026. Os dados de estudo exibidos pertencem à captura. Somente a barra de status e a navegação do Android foram retiradas; o restante da janela do aplicativo foi encaixado proporcionalmente. Nenhum texto da interface foi redesenhado e nenhum resultado de estudo foi inventado.

A imagem 05 mostra a área de questões de um tópico, com o histórico e a opção de iniciar treino. Não é uma captura de questão respondida.

## Recursos Android
A pasta android/res/ contém:
- drawable-nodpi/ic_launcher_folha_fg.png: primeiro plano transparente, 432 × 432.
- drawable-nodpi/ic_launcher_folha_bg.png: fundo opaco, 432 × 432.
- drawable-nodpi/ic_launcher_folha_mono.png: variante temática, 432 × 432.
- mipmap-anydpi-v26/ic_launcher.xml e ic_launcher_round.xml: composição das três camadas.

O conteúdo principal foi encaixado no círculo central seguro de 66 dp dentro da camada de 108 dp, com margem para recortes e efeitos do launcher. Os PNGs de 432 px correspondem à grade de 108 dp em quatro vezes o tamanho.

Para integrar, copie os recursos de android/res/ para os mesmos caminhos em app/src/main/res/. O AndroidManifest atual já usa @mipmap/ic_launcher e @mipmap/ic_launcher_round; o nome do recurso principal é preservado. Os XMLs novos apontam a variante temática para ic_launcher_folha_mono, sem depender do desenho monocromático antigo.

**Esta entrega contém os arquivos prontos para integrar e subir. O código-fonte do app, os recursos existentes, o APK instalado e a listagem publicada não foram substituídos.** A regra do AGENTS.md limita as gravações a conteudo/entrada/, e todo o trabalho do projeto foi mantido nessa pasta.

## Revisão e arquivos de trabalho
- previas/prancha-geral.png: capa, ícone e as seis peças lado a lado.
- previas/icone-mascaras-e-tamanhos.png: máscaras quadrada, circular e arredondada, além de 96, 64 e 48 px e da variante temática.
- previas/*-360.png: leitura das seis peças em escala de celular.
- editaveis/: masters em PNG, incluindo a capa em 2048 × 1000 e camadas do ícone em 1080 × 1080.
- originais/: as três pinturas originais retornadas pelo gerador.
- referencias/capturas-atuais/: capturas integrais usadas na montagem.
- prompts.json e prompt-capa.json: pedidos completos e referências usados.
- manifesto.json: arquivos, dimensões e hashes SHA-256.
- composicao-verificacao.json: recortes, transformações e margens.
- conferencia-visual.json e verificacao-final.json: revisão visual e validação técnica.

A capa original do gerador tem 1794 × 876; o master de trabalho em 2048 × 1000 inclui a tipografia e é reduzido para o arquivo final da loja. O tamanho de trabalho não representa detalhe nativo adicional.

Para reproduzir a montagem no mesmo workspace: node conteudo/entrada/identidade-play-store-2026-10-24/montar-identidade.mjs. Para validar: node conteudo/entrada/identidade-play-store-2026-10-24/verificar-identidade.mjs. Os scripts carregam sharp e @napi-rs/canvas do runtime disponível nesta máquina.

## Direção visual
O caminho feito de páginas transforma a organização dos estudos numa jornada concreta, com o Folha indicando o próximo passo. O ícone usa uma pintura específica em primeiro plano, com rosto amplo, silhueta compacta e duas mãos próximas à capa. A capa usa outra pintura do Folha, em gesto de indicação, para que a cena tenha narrativa própria.

A direção escolhida preserva o personagem reconhecível. Um monograma abstrato perderia esse vínculo; usar uma moldura de conquista no ícone diário adicionaria detalhes de rank sem função para o reconhecimento do aplicativo.

## Especificações consultadas
Os formatos de ícone, capa e capturas seguem a [documentação oficial de recursos de apresentação do Google Play](https://support.google.com/googleplay/android-developer/answer/9866151?hl=en). A grade e o círculo seguro do ícone adaptativo seguem a [documentação oficial do Android](https://developer.android.com/develop/ui/compose/system/icon_design_adaptive).

A página pública da listagem não pôde ser carregada pelo navegador de pesquisa. A revisão da apresentação anterior usou os arquivos do projeto e o XML atual também foi conferido no GitHub. A interface nova foi confirmada diretamente no celular.

