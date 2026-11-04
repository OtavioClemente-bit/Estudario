@echo off
rem Coleta de provas oficiais sem parar: percorre o catalogo do Cebraspe, da FGV, da Fundatec, da Cesgranrio e do
rem Instituto AOCP e repete a cada 24 horas (o que ja foi baixado e pulado; so entram os concursos e gabaritos novos).
rem Depois de cada volta, prepara as questoes novas e acrescenta os pedidos de explicacao na fila do ChatGPT
rem (work\oficiais\fila.json; so acrescenta, nao mexe no que ja foi enviado).
rem Saida em work\provas-oficiais (fora do git). Nada vai para o app sem revisao.
cd /d "%~dp0..\.."
title Provas oficiais - coleta continua
:ciclo
deno run -A scripts\oficiais\cebraspe.ts --catalogo
deno run -A scripts\oficiais\fgv.ts --catalogo
deno run -A scripts\oficiais\fundatec.ts --catalogo
deno run -A scripts\oficiais\cesgranrio.ts --catalogo
deno run -A scripts\oficiais\aocp.ts --catalogo
deno run -A scripts\oficiais\preparar.ts
deno run -A scripts\oficiais\pedidos.ts
echo Volta completa; proxima em 24 horas...
timeout /t 86400 /nobreak >nul
goto ciclo
