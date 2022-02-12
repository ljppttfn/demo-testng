#!/bin/bash

cd /
mkdir -p /hub
mkdir -p /node
cp /selenium-server-standalone-3.141.59.jar /hub/
cp /selenium-server-standalone-3.141.59.jar /node
cd /hub
nohup java -jar selenium-server-standalone-3.141.59.jar -role hub -port 4444 -timeout 30 -browserTimeout 60 -maxSession 20 > hub.log &

cd /node
java -jar selenium-server-standalone-3.141.59.jar -role node -hub http://127.0.0.1:4444/grid/register -maxSession 15 -browser browserName=chrome,maxInstances=10,platform=LINUX > node.log
