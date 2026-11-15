@echo off
rem Conta 1 do Codex escrevendo materias novas (a terca parte da fila), ao lado da janela que decide itens.
rem Decidir itens gasta pouco do limite; sem esta janela a conta 1 ficava quase parada (03/11).
cd /d "%~dp0..\.."
title Codex conta 1 - materias novas (terca parte)
set CODEX_ORDEM=terco
set CODEX_PARALELO=8
set CODEX_ESFORCO=high
set CODEX_CORRECOES=3
:ciclo
rem 14/11: primeiro engrossa os recortes magros (flashcards e memorizacao), depois as materias novas.
rem 15/11: reescrita didatica primeiro (pedido do dono: terminar rapido), depois engrossar e materias novas.
deno run -A scripts\codex\loop-reescrita.ts
deno run -A scripts\codex\loop-engrossar.ts
deno run -A scripts\codex\loop-materias.ts
timeout /t 300 /nobreak >nul
goto ciclo
