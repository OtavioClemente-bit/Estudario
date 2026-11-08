# Emblemas do Estudário — Caderno de conquistas

Coleção de 50 artes, correspondente ao catálogo compartilhado consultado no GitHub em 23/10/2026. Os IDs, nomes, requisitos e níveis vêm de `BadgeCatalog.kt`.

## Direção

Adesivos colecionáveis de caderno com a linguagem oficial da Turma do Folha: contorno índigo, recorte creme, sombra sólida, rostos e bochechas da fonte original, dourado e menta como acentos. Desenho vetorial construído com caminhos e composição, sem imagens sintetizadas. Não se trata de desenho físico feito por um ilustrador humano.

Cada categoria tem sua silhueta; cada conquista recebe uma composição própria, objetos e detalhes relacionados ao seu significado. A faixa mantém as seis cores existentes. O nível 7 de `seq-365` usa esmeralda com uma coroa e uma auréola próprias; não muda o nome de faixa nem inventa requisitos.

Foram consideradas medalhas metálicas como as atuais, ilustrações raster e adesivos vetoriais. A opção vetorial estende a fonte oficial compartilhada, oferece consistência e nitidez e permite conferir cada curva. As medalhas metálicas mantêm a aparência atual, mas não aproximam as conquistas da turma; o raster dificultaria manter a precisão entre 50 peças.

## Entrega e conferência

1. Conferir catálogo e chamadas de desenho do site e Android, incluindo a fonte visual oficial.
2. Compor as 50 artes com IDs preservados e exportar SVG e PNG transparente de 512 px.
3. Disponibilizar galeria responsiva com busca, filtros, temas e detalhe ampliado.
4. Conferir cobertura do catálogo, validade de SVG/JSON, dimensões e transparência das imagens e a renderização da prancha.

Somente arquivos nesta pasta de entrada. Não alterar aplicativo/site, fazer commit, push ou publicar. A instalação nos consumidores fica para o processo autorizado de integração.

## Auditoria do estado anterior

- `shared/.../BadgeCatalog.kt`: 50 entradas em 11 categorias. O tour do Android ainda menciona 44.
- `app/.../profile/BadgeArt.kt`: `BadgeMedal` encaminha para `MedalArt`, e não para o desenho antigo de broches.
- `app/.../profile/MedalArt.kt`: seis paletas e `coerceIn(1, 6)`. A conquista anual é nível 7 e é reduzida a 6.
- `web/.../folha-live.js`: seis paletas em `TIERS`; um nível sem paleta cai no ouro. Assim, o anual pode ter tratamento diferente do Android.
- `design/turma/turma.js`: fonte oficial dos personagens compartilhados. Foi usada como referência e como componentes dos novos desenhos.
- Página pública e entrada de `app.estudario.com.br` conferidas no navegador. A tela privada de conquistas requer login; seu funcionamento foi inspecionado no código, sem acesso à conta.

Fontes: https://github.com/OtavioClemente-bit/Estudario/blob/main/shared/src/commonMain/kotlin/br/com/estudario/domain/BadgeCatalog.kt ; https://github.com/OtavioClemente-bit/Estudario/blob/main/app/src/main/java/br/com/estudario/ui/profile/MedalArt.kt ; https://github.com/OtavioClemente-bit/Estudario/blob/main/design/turma/turma.js
