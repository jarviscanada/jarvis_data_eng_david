#!/bin/bash

###### Define Variables to use below ######
lscpu_out=`\lscpu`
vm_t=`\vmstat -t --unit M`

####### Hardware Specification data  #######
hostname=$(\hostname -f)
cpu_number=$(\echo "$lscpu_out" | \egrep "^CPU\(s\)" | \awk '{print $2}')
cpu_architecture=$(\echo "$lscpu_out" | \egrep "^Architecture:" | \awk '{print $2}')
cpu_model=$(\echo "$lscpu_out" | \egrep "^Model name:" | \awk '{$1=$2=""; \
                print $0;}')
cpu_mhz=$(\echo "$lscpu_out" | \egrep "^CPU MHz:" | \awk '{print $3}')
l2_cache=$(\echo "$lscpu_out" | \egrep "^L2 cache:" | \awk '{print $3}')
timestamp_hw=$(\echo "$vm_t" | \awk 'END {print $(NF-1), $NF}')

