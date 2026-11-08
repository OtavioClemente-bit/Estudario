@echo off
rem Terceiro gerador de questoes: igual ao principal, mas com a SEGUNDA conta do Antigravity.
rem O login dessa conta fica em %USERPROFILE%\.agy-conta2 (separado do login normal).
rem Na primeira vez abre o Antigravity para voce entrar com a outra conta; depois e so deixar a janela aberta.
cd /d "%~dp0..\.."
title Gerador de questoes (Antigravity conta 2) - Estudario
set "AGYHOME=%USERPROFILE%\.agy-conta2"
if not exist "%AGYHOME%" mkdir "%AGYHOME%"
if exist "%AGYHOME%\.gemini\oauth_creds.json" goto volta
echo Entre com a SEGUNDA conta do Antigravity na tela que vai abrir. Depois de entrar, digite /quit e aperte Enter.
setlocal
set "USERPROFILE=%AGYHOME%"
set "HOME=%AGYHOME%"
"%LOCALAPPDATA%\agy\bin\agy.exe"
endlocal
:volta
deno run -A scripts\agy\gerar.ts --conta2
echo Gerador parou; reiniciando em 1 minuto...
timeout /t 60 /nobreak >nul
goto volta
