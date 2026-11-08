@echo off
rem Segunda conta do Codex: escreve materias novas, pegando do FIM da lista (a primeira conta pega do comeco).
rem O login desta conta fica em %USERPROFILE%\.codex-conta2 (separado do login normal em .codex).
rem Na primeira vez abre o navegador para entrar: escolha a OUTRA conta.
cd /d "%~dp0..\.."
title Codex conta 2 - materias novas (do fim da lista)
set CODEX_HOME=%USERPROFILE%\.codex-conta2
if not exist "%CODEX_HOME%" mkdir "%CODEX_HOME%"
if exist "%USERPROFILE%\.codex\config.toml" if not exist "%CODEX_HOME%\config.toml" copy "%USERPROFILE%\.codex\config.toml" "%CODEX_HOME%\config.toml" >nul
if not exist "%CODEX_HOME%\auth.json" (
  echo Entre com a SEGUNDA conta no navegador que vai abrir.
  call codex login
)
set CODEX_ORDEM=reversa
rem 08/11: com a conta 1 sem limite ate 13/11, esta conta pega tambem a parte dela (tirar a linha abaixo quando ela voltar).
set CODEX_PARTES=0,2
set CODEX_PARALELO=24
set CODEX_ESFORCO=high
set CODEX_CORRECOES=3
:ciclo
rem Escreve as materias pendentes; quando acabam, acrescenta e decide os proximos itens (so uma conta decide por vez).
deno run -A scripts\codex\loop-materias.ts
deno run -A scripts\codex\proxima-lista.ts 300
deno run -A scripts\codex\loop-itens.ts
rem Se outra conta estava decidindo, espera um pouco antes de voltar a procurar materias.
timeout /t 300 /nobreak >nul
goto ciclo
