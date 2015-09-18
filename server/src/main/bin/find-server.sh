export PID=`ps -ef | grep "server/conf/billcheckout.xml" | grep -v grep | awk '{print $2}'`
export RMIPID=`ps -ef | grep rmiregistry | grep -v grep | awk '{print $2}'`

if [ -z "${PID}" ]; then
   echo "Servidor de Processamento n�o encontrado."
else
   echo "Servidor de Processamento: ${PID}"
fi

if [ -z "${RMIPID}" ]; then
   echo "RMIRegistry n�o encontrado."
else
   echo "RMIRegistry: ${RMIPID}"
fi


