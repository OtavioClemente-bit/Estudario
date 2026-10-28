@echo off
rem Mantém o Codex trabalhando sem parar na lista de itens de edital sem matéria. Deixe esta janela aberta.
rem Para parar: feche a janela ou Ctrl+C. Para retomar: dois cliques de novo (continua de onde parou).
cd /d "%~dp0..\.."
title Codex sem parar - itens sem materia
deno run -A scripts\codex\loop-itens.ts
pause
