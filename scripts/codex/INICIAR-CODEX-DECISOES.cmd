@echo off
rem Conta 1 do Codex so DECIDE itens de edital sem materia (ligar, materia nova, amplo, ignorar).
rem Antes ela escrevia materias e so decidia quando a fila de materias acabava, o que quase nunca acontecia:
rem as decisoes andavam ~30 por dia (03/11). Quem escreve materias agora sao as contas 2 e 3.
rem Quando a lista acaba, acrescenta os proximos 300 itens sem materia e continua. Para parar: feche a janela.
cd /d "%~dp0..\.."
title Codex conta 1 - decide itens de edital
set CODEX_ESFORCO=high
:ciclo
deno run -A scripts\codex\loop-itens.ts
deno run -A scripts\codex\proxima-lista.ts 300
if errorlevel 3 (
  echo Nenhum item novo sem materia; esperando 1 hora para conferir de novo.
  timeout /t 3600 /nobreak >nul
)
timeout /t 10 /nobreak >nul
goto ciclo
