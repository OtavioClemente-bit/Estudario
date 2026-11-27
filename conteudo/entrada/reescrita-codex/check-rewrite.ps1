$source = Get-Content -Raw -Encoding utf8 'conteudo/materias/maranhao/regimento-interno-tce-ma.json' | ConvertFrom-Json
$target = Get-Content -Raw -Encoding utf8 'conteudo/entrada/reescrita-codex/maranhao.regimento-interno-tce-ma.json' | ConvertFrom-Json
$lengths = @($target.chapters | ForEach-Object { $_.markdown.Length })+$sum = ($lengths | Measure-Object -Sum).Sum
$otherFields = $source.PSObject.Copy()
$targetFields = $target.PSObject.Copy()
$otherFields.PSObject.Properties.Remove('version')
$targetFields.PSObject.Properties.Remove('version')
foreach ($chapter in @($otherFields.chapters)) { $chapter.PSObject.Properties.Remove('markdown') }
foreach ($chapter in @($targetFields.chapters)) { $chapter.PSObject.Properties.Remove('markdown') }
$same = (ConvertTo-Json $otherFields -Depth 100 -Compress) -eq (ConvertTo-Json $targetFields -Depth 100 -Compress)
"version=$($target.version) chars=$sum chapterLengths=$($lengths -join ',') titlesUnchanged=$( (($source.chapters.title -join '|') -eq ($target.chapters.title -join '|')) ) questionsUnchanged=$( ((ConvertTo-Json $source.questions -Depth 100 -Compress) -eq (ConvertTo-Json $target.questions -Depth 100 -Compress)) ) otherFieldsUnchanged=$same"
