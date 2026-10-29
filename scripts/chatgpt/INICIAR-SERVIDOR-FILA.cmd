@echo off
rem Ponte do Tampermonkey (ChatGPT no Chrome) com a fila do projeto. Deixe esta janela aberta.
rem Se cair, volta sozinho em 1 minuto.
cd /d "%~dp0..\.."
title Fila do ChatGPT - Estudario
:volta
deno run -A scripts\chatgpt\servidor-fila.ts
echo Servidor parou; reiniciando em 1 minuto...
timeout /t 60 /nobreak >nul
goto volta
