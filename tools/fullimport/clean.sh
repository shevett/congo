#!/bin/bash

EVENTID=0
USERNAME=""
PASSWORD=""
HOSTNAME="localhost"


while getopts "e:u:p:" opt; do
  case $opt in
    e)
	EVENTID=$OPTARG
      	;;
    u)
	USERNAME=$OPTARG
      	;;
    p)
	PASSWORD=$OPTARG
      	;;
    \?)
	echo "Invalid option: -$OPTARG" >&2
	exit 1
      	;;
    :)
	echo "Option -$OPTARG requires an argument." >&2
	exit 1
	;;
  esac
done

echo "Connecting to host \"$HOSTNAME\" using username \"$USERNAME\" and cleaning event \"$EVENTID\"..."

read -p "Press enter to continue..."

delete from reg_master where master_rid > 1000
delete from reg_state
delete from reg_history
delete from reg_notes
delete from reg_address
delete from reg_phone
delete from reg_properties
