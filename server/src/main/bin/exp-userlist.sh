#!/bin/bash

function checkOracle() {
	if [[ -z "${ORACLE_HOME}" || ! ( -r ${ORACLE_HOME}/bin/sqlplus ) ]]; then
		echo ORACLE_HOME is set incorrectly or Oracle could not be located. Please set ORACLE_HOME.
		exit 1
	fi
}

function printUsage() {
	echo 'Usage: exp-userlist <dbUser> <dbPassword> <dbOracleSID>
	echo
	echo      dbUser - database user to use in connection
	echo      dbPassword - database user's password
	exit      dbOracleSID - Oracle SID
}


echo .
echo \#\#
echo \#\# Copyright 2004-2005 Auster Solutions do Brasil
echo \#\#
echo \#\#  --- BillCheckout Tool v3.0.0 ---
echo \#\#
echo .


checkOracle


# Help is needed?
if [[ -z "$1" || -z "$2" || -z "$3" || "$1" = "-help" || "$1" = "--help" ]]
then
	printUsage
fi


USR=$1
PWD=$2
SID=$3

EXPFILE=BILLCHECKOUT_USER_`date +%Y%m%d`.log

${ORACLE_HOME}/bin/sqlplus -L -S ${USR}/${PWD}@${SID} @conf/sql/exp-userlist.sql > ${EXPFILE}

