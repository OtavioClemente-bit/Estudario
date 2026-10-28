@echo off
rem Gerador contínuo de questões (Antigravity: Argon escreve, Opus audita). Deixe esta janela aberta.
rem Para parar: feche a janela ou aperte Ctrl+C. Para retomar: dê dois cliques neste arquivo de novo.
cd /d "%~dp0..\.."
title Gerador de questoes - Estudario
deno run -A scripts\agy\gerar.ts
pause
