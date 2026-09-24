$ErrorActionPreference = 'Stop'
$repo = (Resolve-Path (Join-Path $PSScriptRoot '..\..')).Path
$npx = 'C:\Program Files\nodejs\npx.cmd'
$out1 = Join-Path $env:TEMP 'task12-delete-session1.out'
$err1 = Join-Path $env:TEMP 'task12-delete-session1.err'
$out2 = Join-Path $env:TEMP 'task12-delete-session2.out'
$err2 = Join-Path $env:TEMP 'task12-delete-session2.err'

function Invoke-Supabase([string[]] $Arguments) {
    & $npx @Arguments --workdir $repo
    if ($LASTEXITCODE -ne 0) { throw "supabase command failed ($LASTEXITCODE): $($Arguments -join ' ')" }
}

function Invoke-LocalSql([string] $File) {
    Get-Content -Raw (Join-Path $repo $File) | docker exec -i supabase_db_estudario-local psql -U postgres -d postgres -v ON_ERROR_STOP=1
    if ($LASTEXITCODE -ne 0) { throw "local SQL fixture failed: $File" }
}

Invoke-Supabase @('--yes', 'supabase', 'db', 'reset')
Invoke-LocalSql 'scripts/task12/private_syllabus_delete_concurrency_setup.sql'

$p1 = Start-Process -FilePath 'cmd.exe' -ArgumentList @('/c', "docker exec -i supabase_db_estudario-local psql -U postgres -d postgres -v ON_ERROR_STOP=1 < `"$(Join-Path $repo 'scripts/task12/private_syllabus_delete_concurrency_session1.sql')`"") -RedirectStandardOutput $out1 -RedirectStandardError $err1 -PassThru -WindowStyle Hidden
Start-Sleep -Milliseconds 300
$p2 = Start-Process -FilePath 'cmd.exe' -ArgumentList @('/c', "docker exec -i supabase_db_estudario-local psql -U postgres -d postgres -v ON_ERROR_STOP=1 < `"$(Join-Path $repo 'scripts/task12/private_syllabus_delete_concurrency_session2.sql')`"") -RedirectStandardOutput $out2 -RedirectStandardError $err2 -PassThru -WindowStyle Hidden
$p1.WaitForExit()
$p2.WaitForExit()
if ($p1.ExitCode -ne 0) { throw "session 1 failed: $(Get-Content $err1 -Raw)" }
if ($p2.ExitCode -ne 0) { throw "session 2 failed: $(Get-Content $err2 -Raw)" }
if ((Get-Content $out1 -Raw) -notmatch 'SYNCED' -or (Get-Content $out2 -Raw) -notmatch 'SYNCED') { throw 'both DB sessions must receive SYNCED ACKs' }

Invoke-LocalSql 'scripts/task12/private_syllabus_delete_concurrency_assert.sql'
