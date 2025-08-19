#!/bin/bash
while :;
do
	java -server -Dfile.encoding=UTF-8 -Xmx6G -cp config:./lib/* l2s.gameserver.GameServer > stdout.log 2>&1
	[ $? -ne 2 ] && break
	sleep 30;
done