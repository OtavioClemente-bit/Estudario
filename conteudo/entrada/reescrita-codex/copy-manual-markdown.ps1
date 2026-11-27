$path = 'conteudo/entrada/reescrita-codex/maranhao.regimento-interno-tce-ma.json'
$matter = Get-Content -Raw -Encoding utf8 $path | ConvertFrom-Json
for ($i = 0; $i -lt 4; $i++) {
    $markdownPath = "conteudo/entrada/reescrita-codex/c$($i + 1).md"
    $matter.chapters[$i].markdown = (Get-Content -Raw -Encoding utf8 $markdownPath).TrimEnd("`r", "`n")
}
$json = $matter | ConvertTo-Json -Depth 100
[System.IO.File]::WriteAllText((Resolve-Path $path), $json + [Environment]::NewLine, [System.Text.UTF8Encoding]::new($false))
