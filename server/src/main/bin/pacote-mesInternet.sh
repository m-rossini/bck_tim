#!/bin/sh


function listPackages() {
  echo > ${COMMANDFILE}
  echo "SET PAGESIZE 2000;" >> ${COMMANDFILE}
  echo "SET LINESIZE 2000;" >> ${COMMANDFILE}
  echo "SELECT SHORT_DESC as \"Shdes\", DESCRIPTION as \"Nome do Pacote\" FROM QLF_PACKAGE WHERE CUSTOM_1 IS NOT NULL;" >> ${COMMANDFILE}
  echo "EXIT;" >> ${COMMANDFILE}
}

function addPackage() {
  if [[ -z "${PACKAGE}" ]]; then
	  displayUsage
  fi
  echo > ${COMMANDFILE}
  echo "SET PAGESIZE 2000;" >> ${COMMANDFILE}
  echo "SET LINESIZE 2000;" >> ${COMMANDFILE}
  echo "UPDATE QLF_PACKAGE SET CUSTOM_1='T' WHERE SHORT_DESC = '${PACKAGE}';" >> ${COMMANDFILE}
  echo "COMMIT;" >> ${COMMANDFILE}
  echo "EXIT;" >> ${COMMANDFILE}
}

function removePackage() {
  if [[ -z "${PACKAGE}" ]]; then
	  displayUsage
  fi
  echo > ${COMMANDFILE}
  echo "SET PAGESIZE 2000;" >> ${COMMANDFILE}
  echo "SET LINESIZE 2000;" >> ${COMMANDFILE}
  echo "UPDATE QLF_PACKAGE SET CUSTOM_1=NULL WHERE SHORT_DESC = '${PACKAGE}';" >> ${COMMANDFILE}
  echo "COMMIT;" >> ${COMMANDFILE}
  echo "EXIT;" >> ${COMMANDFILE}
}


function readPassword() {
  echo "Senha para ${DB_USER}:"
  oldmodes=`stty -g`
  stty -echo
  read DB_PWD
  stty $oldmodes
}

function runSQLCommands() {
  ${ORACLE_HOME}/bin/sqlplus -S ${DB_USER}/${DB_PWD}@${ORACLE_SID} @${COMMANDFILE}
}

function displayUsage() {
	echo "Uso: pacote-mesInternet.sh <COMANDO> [<ID-DO-PACOTE>]"
	exit 1
}

###
#  Inicio do script
###

COMMANDFILE=mesinternet.sql
DB_USER=TIM_BCK
OPTION=$1
PACKAGE=$2

if [[ -z "${ORACLE_HOME}" || ! ( -r ${ORACLE_HOME}/bin/sqlplus ) ]]; then
	echo "ORACLE_HOME esta definido incorretamente ou o comando sqlplus nao pode ser encontrado. Configure corretamente a variavel de ambiente ORACLE_HOME."
	exit 1
fi

if [[ -z "${ORACLE_SID}" ]]; then
	echo "ORACLE_SID nao foi definido, portanto o script nao conseguira conectar-se ao banco de dados."
	exit 1
fi

if [[ -z "${OPTION}" ]]; then
  displayUsage
fi

case ${OPTION} in
  l) listPackages;;
  a) addPackage;;
  d) removePackage;;
  *) echo "Opcao invalida. Use uma entre (l)istar, (a)dicionar ou (d)eletar"; exit 2;;
esac

readPassword

runSQLCommands

rm ${COMMANDFILE}

exit 0