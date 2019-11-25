#!/bin/bash

###### Define Variables to use below ######
vm_t=`\vmstat -t --unit M`


####### Linux Resource #######
timestamp_lr=$(\echo "$vm_t" | \awk 'END {print $(NF-1), $NF}')
memory_free=$(\echo "$vm_t" | \awk 'END {print $4}')
cpu_idle=$(\echo "$vm_t" |\awk 'END {print $15}')
cpu_kernel=$(\echo "$vm_t" |\awk 'END {print $14}')
disk_io=`vmstat -d --unit M | \awk 'END {print $(NF-1)}'`
disk_available=$(\df -BM / | \awk END{print} | \awk '{print $4}' | \sed s/[^0-9]*//g)
