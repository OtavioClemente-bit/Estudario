@echo off
rem Conta 1 do Codex escrevendo materias novas (a terca parte da fila), ao lado da janela que decide itens.
rem Decidir itens gasta pouco do limite; sem esta janela a conta 1 ficava quase parada (03/11).
cd /d "%~dp0..\.."
title Codex conta 1 - materias novas (terca parte)
set CODEX_ORDEM=terco
set CODEX_PARALELO=12
set CODEX_ESFORCO=high
set CODEX_CORRECOES=3
:ciclo
deno run -A scripts\codex\loop-materias.ts
timeout /t 300 /nobreak >nul
goto ciclo
