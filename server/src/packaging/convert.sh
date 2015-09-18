osName=`uname`

sourceDir=$1
returnDir=$2
fileMask=$3

if [ ${osName} = "Linux" ]; then
	cd ${sourceDir}
	for file in `ls ${fileMask}` 
	do
		echo "Converting ${file}"
		iconv -f iso-8859-1 -t utf-8 ${file} > ${file}.tmp
		mv ${file}.tmp ${file}
	done
else
	echo "Not a UNIX environment. Skipping charset conversion"
fi

cd ${returnDir}
