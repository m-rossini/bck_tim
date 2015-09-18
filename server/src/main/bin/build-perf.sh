#/bin/bash

cat $1 | grep "Finished processing filter" | awk '{print $1 $6 $4 $5 $6 $14 $6 $16}' > $2-filters.txt

cat $1 | grep "Total processing time" | awk '{print $1 $6 $4 $5 $6 $14 $15}' > $2-total.txt 


