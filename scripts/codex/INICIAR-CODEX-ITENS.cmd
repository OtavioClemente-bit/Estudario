@echo off
rem Codex sem parar, em ciclo: (1) escreve e corrige as mat?rias novas que faltam; (2) quando acabam, acrescenta
rem ? lista os pr?ximos itens de edital sem mat?ria; (3) decide esses itens; volta ao (1).
rem Se um passo cair, o ciclo segue. Deixe esta janela aberta. Para parar: feche ou Ctrl+C.
cd /d "%~dp0..\.."
title Codex sem parar - materias novas
rem 16 materias ao mesmo tempo, esforco alto e so 1 correcao: o que nao passar fica para a revisao do Claude.
set CODEX_PARALELO=24
set CODEX_ESFORCO=high
set CODEX_CORRECOES=3
:ciclo
deno run -A scripts\codex\loop-materias.ts
deno run -A scripts\codex\proxima-lista.ts 300
if errorlevel 3 (
  echo Nenhum item novo sem materia; esperando 1 hora para conferir de novo.
  timeout /t 3600 /nobreak >nul
)
deno run -A scripts\codex\loop-itens.ts
timeout /t 10 /nobreak >nul
goto ciclo
