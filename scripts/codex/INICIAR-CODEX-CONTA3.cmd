@echo off
rem Terceira conta do Codex: REESCRITA DIDATICA das materias antigas (conteudo/fila/reescrita-didatica-faltam.txt).
rem Grava em conteudo/entrada/reescrita-codex; o Claude revisa e publica. Quando a fila de reescrita acaba, passa a
rem escrever materias novas (do meio da lista, como reserva das contas 1 e 2).
rem O login desta conta fica em %USERPROFILE%\.codex-conta3 (separado do login normal em .codex).
cd /d "%~dp0..\.."
title Codex conta 3 - reescrita didatica
set CODEX_HOME=%USERPROFILE%\.codex-conta3
if not exist "%CODEX_HOME%" mkdir "%CODEX_HOME%"
if exist "%USERPROFILE%\.codex\config.toml" if not exist "%CODEX_HOME%\config.toml" copy "%USERPROFILE%\.codex\config.toml" "%CODEX_HOME%\config.toml" >nul
if not exist "%CODEX_HOME%\auth.json" (
  echo Entre com a TERCEIRA conta no navegador que vai abrir.
  call codex login
)
set CODEX_ORDEM=meio
set CODEX_PARALELO=24
set CODEX_ESFORCO=high
set CODEX_CORRECOES=3
:ciclo
deno run -A scripts\codex\loop-materias.ts
timeout /t 300 /nobreak >nul
goto ciclo
