@echo off
rem Segundo gerador, em paralelo: escreve pela API gratis do Google (Flash) e audita pelo Antigravity.
rem Precisa do arquivo .env.google na pasta do projeto. Deixe esta janela aberta; Ctrl+C para parar.
cd /d "%~dp0..\.."
title Gerador de questoes (API Google) - Estudario
deno run -A scripts\agy\gerar.ts --api
pause
