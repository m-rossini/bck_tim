#! /bin/sh 

echo
echo \#\#
echo \#\# Copyright 2004-2006 Auster Solutions do Brasil
echo \#\#
echo \#\#  --- BillCheckout Tool v3.0.0 ---
echo \#\#
echo


DB_USER=billchkdb
DB_PWD=billchkdb

DATAFILE=$1

if [[ -z "${DATAFILE}" || ! ( -r ${DATAFILE} ) ]]; then
	echo Usage: guiding_loader.sh [datafile]
	exit 1
fi

if [[ -z "${ORACLE_HOME}" || ! ( -r ${ORACLE_HOME}/bin/sqlldr ) ]]; then
	echo ORACLE_HOME is set incorrectly or sqlldr could not be located. Please set ORACLE_HOME.
	exit 1
fi


echo Running loader for tariff zone
${ORACLE_HOME}/bin/sqlldr userid=${DB_USER}/${DB_PWD} readsize=20000000 bindsize=20000000 errors=99999999 control=ldr/tariffzone.ldr data=${DATAFILE} log=log/tzone.log
