#!/bin/bash
nohup java -jar -Duser.timezone=UTC target/qdm-care-giver-clients-0.0.1-SNAPSHOT.jar > logs/application.txt 2>&1 &
echo $! > pid.file
