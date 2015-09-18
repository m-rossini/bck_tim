#! /bin/sh
export PORT=2004

echo
echo Starting RMI Registry
echo
echo Running RMI Registry on port $PORT
echo

export CLASSPATH=$( ls -1 ${BCK_BASEDIR}/server/lib/tim-billcheckout-server-*.jar )

${JAVA_HOME}/bin/rmiregistry $PORT

