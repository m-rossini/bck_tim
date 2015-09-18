#! /bin/sh

echo
echo \#\#
echo \#\# Copyright 2004-2005 Auster Solutions do Brasil
echo \#\#
echo \#\#  --- BillCheckout Tool v3.0.0 ---
echo \#\#
echo

function checkAnt {
	if [[ -z "${ANT_HOME}" || ! ( -r ${ANT_HOME}/lib/ant.jar ) ]]; then
		echo ANT_HOME is set incorrectly or Ant could not be located. Please set ANT_HOME.
		exit 1
	fi
}

function checkJava {
	if [[ -z "${JAVA_HOME}" || ! ( -r ${JAVA_HOME}/bin/java ) ]]; then
		echo JAVA_HOME is set incorrectly or java could not be located. Please set JAVA_HOME.
		exit 1
	fi
}


checkAnt
checkJava

inputFiles=$1
	if [[ -z "${inputFiles}" || ! ( -r ${inputFiles} ) ]]; then
		echo "Usage: microcell-loader.sh <input-file>"
		exit 2
	fi


${JAVA_HOME}/bin/java \
  -cp ${ANT_HOME}/lib/ant-launcher.jar \
  -Dant.home=${ANT_HOME} \
  -Dbasedir=${BCK_BASEDIR}/cmdline \
  -Dcmdline.args="${inputFiles}" \
  org.apache.tools.ant.launch.Launcher \
  -Dloader.db=conf/loader.xml \
  -Dloader.layout=conf/microcell/microcell.properties \
  -Dlog4j.configuration=conf/microcell/log4j.xml \
  -file ${BCK_BASEDIR}/cmdline/bin/loader-run.xml

echo
echo [ Finished ]
echo
