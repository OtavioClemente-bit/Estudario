$ErrorActionPreference = 'Stop'
$repo = (Resolve-Path (Join-Path $PSScriptRoot '..\..')).Path
$npx = 'C:\Program Files\nodejs\npx.cmd'

function Invoke-Supabase([string[]] $Arguments) {
    & $npx @Arguments --workdir $repo
    if ($LASTEXITCODE -ne 0) { throw "supabase command failed ($LASTEXITCODE): $($Arguments -join ' ')" }
}

function Invoke-LocalSql([string] $File) {
    Get-Content -Raw (Join-Path $repo $File) | docker exec -i supabase_db_estudario-local psql -U postgres -d postgres -v ON_ERROR_STOP=1
    if ($LASTEXITCODE -ne 0) { throw "local SQL fixture failed: $File" }
}

# This is an executable upgrade harness, not a pg_proc signature check:
# reset stops at 012, seed writes through the historical function, migration up
# applies 013/014, and the assertion reads the same database afterwards.
Invoke-Supabase @('--yes', 'supabase', 'db', 'reset', '--version', '202609240012')
Invoke-LocalSql 'scripts/task12/private_syllabus_incremental_seed.sql'
Invoke-Supabase @('--yes', 'supabase', 'migration', 'up', '--local')
Invoke-LocalSql 'scripts/task12/private_syllabus_incremental_assert.sql'
