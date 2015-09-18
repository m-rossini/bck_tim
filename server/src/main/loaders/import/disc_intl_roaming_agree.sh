#!/bin/sh

echo
echo \#\#
echo \#\# Copyright 2004-2006 Auster Solutions do Brasil
echo \#\#
echo \#\#  --- BillCheckout Tool v3.0.0 ---
echo \#\#
echo

# +---------------------------------------+
# | Altere as configura��es abaixo        |
# |   conforme explica��o:                |
# |                                       |
# |   * DB_USER: usu�rio de banco de      |
# |   dados do BILLCHECKOUT.              |
# |                                       |
# |   * DB_PWD: senha do usu�rio definido |
# |   em DB_USER                          |
# |                                       |
# |   * DB_SID: nome do banco de dados    |
# |   do BILLCHECKOUT                     |
# +---------------------------------------+

BSCS_USER=bscs
DB_USER=billchkdb
DB_PWD=billchkdb
DB_SID=billp1

DATAFILE=bck_disc_intl_roaming_agree.dump

if [[ -z "${ORACLE_HOME}" || ! ( -r ${ORACLE_HOME}/bin/sqlldr ) ]]; then
	echo A vari�vel de ambiente ORACLE_HOME n�o est� definida corretamente ou a instala��o do Oracle n�o pode ser encontrada.
	exit 1
fi

echo Executando o processo de importa��o de dados para a tabela  DISC_INTL_ROAMING_AGREE
${ORACLE_HOME}/bin/sqlldr userid=${DB_USER}/${DB_PWD}@${DB_SID} readsize=20000000 bindsize=20000000 errors=99999999 direct=y data=${DATAFILE} log=${DATAFILE}.log bad=${DATAFILE}.bad discard=${DATAFILE}.dsc control=ldr/disc_intl_roaming_agree.ldr

