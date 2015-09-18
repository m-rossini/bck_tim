@echo off

echo.
echo ##
echo ## Copyright 2004-2005 Auster Solutions do Brasil
echo ##
echo ##  --- BillCheckout Tool v3.0.0 ---
echo ##
echo.

set DB_USER=test
set DB_PWD=test

set DATAFILE=%1

if "%DATAFILE%"=="" goto usageHelp
if "%ORACLE_HOME%"=="" goto noOracle
if not exist "%ORACLE_HOME%\bin\sqlldr.exe" goto noOracle
goto runScript

:usageHelp
echo Usage: guiding_loader.sh [datafile]
goto end

:noOracle
echo ORACLE_HOME is set incorrectly or sqlldr could not be located. Please set ORACLE_HOME.
goto end


:runScript

@REM echo Running loader for rate timezone
@REM %ORACLE_HOME%/bin/sqlldr.exe userid=%DB_USER%/%DB_PWD% readsize=20000000 bindsize=20000000 errors=99999999 control=ldr/ratetimezone.ldr data=%DATAFILE% log=log/ttz_guiding.log

set NLS_LANG=AMERICAN
set NLS_TERRITORY=AMERICA

echo Running loader for guiding
%ORACLE_HOME%/bin/sqlldr.exe userid=%DB_USER%/%DB_PWD% readsize=20000000 bindsize=20000000 errors=99999999 control=ldr/guiding.ldr data=%DATAFILE% log=log/guiding.log


:end
