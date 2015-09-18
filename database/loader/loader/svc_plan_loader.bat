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

echo Running loader for services
%ORACLE_HOME%/bin/sqlldr.exe userid=%DB_USER%/%DB_PWD% readsize=20000000 bindsize=20000000 errors=99999999 control=ldr/services.ldr data=%DATAFILE% log=log/services.log

echo Running loader for rate plans
%ORACLE_HOME%/bin/sqlldr.exe userid=%DB_USER%/%DB_PWD% readsize=20000000 bindsize=20000000 errors=99999999 control=ldr/plans.ldr data=%DATAFILE% log=log/plans.log

echo Running loader for service plan association
%ORACLE_HOME%/bin/sqlldr.exe userid=%DB_USER%/%DB_PWD% readsize=20000000 bindsize=20000000 errors=99999999 control=ldr/svc_plans.ldr data=%DATAFILE% log=log/svcplans.log 

:end
