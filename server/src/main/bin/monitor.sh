#/bin/bash
##################################
#
# This script monitors Java processes, saving memory, CPU, GC and heap information during its execution.
# 
# Usage: monitor.sh <pid> <sleep> <checks> <prefix>
#
#    pid : PID of the monitored process; mandatory and no default value
#    sleep : number of seconds to wait between checks (default 10)
#    checks : number of checks to make (default 1000)
#    prefix : string to prefix generated log files (default 'monitor')
#
##################################




PID=$1
if [ ${PID} = "" ]; then
	echo "PID parameter not informed. Check script usage."
	exit 1
fi

SLEEP=$2
if [${SLEEP} -z ]; then
   SLEEP=10
fi

LIMIT=$3
if [${LIMIT} -z ]; then
   LIMIT=1000
fi  
 
PREFIX=$4
if [${PREFIX} -z ]; then
   PREFIX=monitor
fi   
 

# cleaning previous executions

DATE_LOG=${PREFIX}-date.log
echo >  ${DATE_LOG}
PS_LOG=${PREFIX}-ps.log
echo > ${PS_LOG}
VMSTAT_LOG=${PREFIX}-vmstat.log
echo > ${VMSTAT_LOG}
JSTAT_LOG=${PREFIX}-jstat.log
echo > ${JSTAT_LOG}
HEAP_LOG=${PREFIX}-heap.log
echo > ${HEAP_LOG}


# starting to monitor
var=0
while (( var < LIMIT ))
do
  # save date
  echo ${var}: `date` >> ${DATE_LOG}
  # check memory
  echo " +----- Execution #${var} -----+" >>  ${PS_LOG}
  ps -efo pcpu,pmem,comm,vsz,nice,etime,pid | grep ${PID} >> ${PS_LOG}
  echo " +-----------------------------+" >>  ${PS_LOG}
  # check vmstat
  echo " +----- Execution #${var} -----+" >>  ${VMSTAT_LOG}
  vmstat >> ${VMSTAT_LOG}
  echo " +-----------------------------+" >>  ${VMSTAT_LOG}
  # check jstat
  echo " +----- Execution #${var} -----+" >>  ${JSTAT_LOG}
  jstat -gcutil ${PID} 1 1 >> ${JSTAT_LOG}
  echo " +-----------------------------+" >>  ${JSTAT_LOG}
  # check heap
  echo " +----- Execution #${var} -----+" >>  ${HEAP_LOG}
  jmap -heap ${PID} >> ${HEAP_LOG}
  echo " +-----------------------------+" >>  ${HEAP_LOG}
	
  # sleeping
  (( var++ ))
  sleep ${SLEEP}
done 

exit 0
