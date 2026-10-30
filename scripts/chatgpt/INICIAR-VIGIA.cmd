@echo off
rem Vigia do ChatGPT: reabre o Chrome se ficar 60 min sem arquivo novo. Deixe esta janela aberta.
cd /d "%~dp0..\.."
title Vigia do ChatGPT - Estudario
:volta
deno run -A scripts\chatgpt\vigia.ts
timeout /t 60 /nobreak >nul
goto volta
