@echo off
rem Fase 2: mantém o Codex escrevendo, uma por vez, as matérias novas dos itens de edital sem matéria.
rem Deixe esta janela aberta. Para parar: feche ou Ctrl+C. Para retomar: dois cliques de novo.
cd /d "%~dp0..\.."
title Codex sem parar - materias novas
deno run -A scripts\codex\loop-materias.ts
pause
