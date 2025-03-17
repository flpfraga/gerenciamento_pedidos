@echo off
setlocal enabledelayedexpansion

rem Define o diretório onde as migrations serão salvas
set "MIGRATION_DIR=src\main\resources\db\migrations"

rem Cria o diretório se não existir
if not exist "%MIGRATION_DIR%" mkdir "%MIGRATION_DIR%"

rem Obtém o timestamp no formato YYYYMMDDHHMMSS
for /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /value') do set "TIMESTAMP=%%I"
set "TIMESTAMP=%TIMESTAMP:~0,14%"

rem Solicita o nome da migração ao usuário
set /p MIGRATION_NAME=Digite o nome da migração (use _ para separar palavras): 

rem Substitui espaços por underscores (caso o usuário tenha digitado espaços por engano)
set "MIGRATION_NAME=!MIGRATION_NAME: =_!"

rem Define o nome do arquivo
set "MIGRATION_FILE=%MIGRATION_DIR%\V%TIMESTAMP%__%MIGRATION_NAME%.sql"

rem Cria o arquivo vazio
type nul > "%MIGRATION_FILE%"

echo Migration criada: %MIGRATION_FILE%
