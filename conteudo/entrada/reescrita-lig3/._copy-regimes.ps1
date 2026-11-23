$targetFile = 'conteudo/entrada/reescrita-lig3/direito-tributario.regimes-especiais.json'
$matter = Get-Content -Raw -Encoding UTF8 $targetFile | ConvertFrom-Json
$matter.chapters[0].markdown = Get-Content -Raw -Encoding UTF8 'conteudo/entrada/reescrita-lig3/._regimes-c1.md'
$matter.chapters[1].markdown = Get-Content -Raw -Encoding UTF8 'conteudo/entrada/reescrita-lig3/._regimes-c2.md'
$matter.chapters[2].markdown = Get-Content -Raw -Encoding UTF8 'conteudo/entrada/reescrita-lig3/._regimes-c3.md'
$matter.chapters[3].markdown = Get-Content -Raw -Encoding UTF8 'conteudo/entrada/reescrita-lig3/._regimes-c4.md'
$matter.chapters[4].markdown = Get-Content -Raw -Encoding UTF8 'conteudo/entrada/reescrita-lig3/._regimes-c5.md'
$matter.version = 3
$serialized = ConvertTo-Json -InputObject $matter -Depth 100
Set-Content -LiteralPath $targetFile -Value $serialized -Encoding UTF8
