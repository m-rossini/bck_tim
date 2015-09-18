#!/bin/bash

echo
echo \#\#
echo \#\# Copyright 2007 Auster Solutions do Brasil
echo \#\#
echo \#\#  --- Usage Loader for Duplicity Validation ---
echo \#\#
echo

function checkAnt {
	if [[ -z ${ANT_HOME} || ! ( -r ${ANT_HOME}/lib/ant.jar ) ]]
	then
		echo ANT_HOME is set incorrectly or Ant could not be located. Please set ANT_HOME.
		exit 1
	fi
}

function checkJava {
	if [[ -z ${JAVA_HOME} || ! ( -r ${JAVA_HOME}/bin/java ) ]]
	then
		echo JAVA_HOME is set incorrectly or java could not be located. Please set JAVA_HOME.
		exit 1
	fi
}

function printUsage {
	echo 'Usage: load-usages -f FILES'
	echo
	echo '    -f   filename pattern of all files to be processed'
	echo
	exit 1
}

# Script invoked with no command-line args?
if [[ $# -eq 0 ]]
then
	printUsage
fi

# Help is needed?
if [[ -z "$1" || "$1" = "-help" || "$1" = "--help" ]]
then
	printUsage
fi


INPUT_FILES=

while getopts "f:" OPTION
do
	case $OPTION in
	    f ) INPUT_FILES="$OPTARG";;
          [?] ) printUsage;;
	esac
done
shift $(($OPTIND - 1))

if [[ -z $INPUT_FILES ]]
then
	echo "[ERROR] Input files not set"
	exit 1
fi

checkAnt
checkJava


REMOVEAFTER=95

echo \#\#
echo \#\# Iniciando a etapa de limpeza de arquivos anteriores a ${REMOVEAFTER} dias
echo \#\#

find ${BCK_LOADEROUTPUTDIR}/usage-history \
 -mtime +${REMOVEAFTER} \
 -type f \
 -exec rm -f {} \;


echo \#\#
echo \#\# Etapa de limpeza finalizada. Iniciando a etapa de carga.
echo \#\#

${JAVA_HOME}/bin/java \
  -cp ${ANT_HOME}/lib/ant-launcher.jar \
  -Dant.home=${ANT_HOME} \
  -Dbasedir=${BCK_BASEDIR}/server \
  -Dinput.file.path="${INPUT_FILES}" \
  org.apache.tools.ant.launch.Launcher \
  -file ${BCK_BASEDIR}/server/bin/load-usages.xml

if [[ $? -ne 0 ]]
    then
    echo [ERRO] Ocorreu um erro durante a execucao do load-usages.
    echo Para maiores informacoes, verifique os arquivos de LOG.
    exit 1
fi

echo
echo [ Finished ]
echo
