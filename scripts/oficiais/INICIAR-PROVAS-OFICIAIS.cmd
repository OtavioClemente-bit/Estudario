@echo off
rem Coleta de provas oficiais sem parar: percorre o catalogo inteiro do Cebraspe, da FGV e da Fundatec e repete a
rem cada 24 horas (o que ja foi baixado e pulado; so entram os concursos e gabaritos novos).
rem Saida em work\provas-oficiais (fora do git). Nada vai para o app sem revisao.
cd /d "%~dp0..\.."
title Provas oficiais - coleta continua
:ciclo
deno run -A scripts\oficiais\cebraspe.ts --catalogo
deno run -A scripts\oficiais\fgv.ts --catalogo
deno run -A scripts\oficiais\fundatec.ts --catalogo
echo Volta completa; proxima em 24 horas...
timeout /t 86400 /nobreak >nul
goto ciclo
