#!/bin/bash
# usage : source starter/Linux.sh
# 
# bash
    # export GOOGLE_APPLICATION_CREDENTIALS="./src/main/java/com/example/restservice/Transcription/credential/application_default_credentials.json"
    # echo "GOOGLE_APPLICATION_CREDENTIALS = " $GOOGLE_APPLICATION_CREDENTIALS
    # 裡面有外面沒有
# 
# source
set GOOGLE_APPLICATION_CREDENTIALS="./src/main/java/com/example/restservice/Transcription/credential/application_default_credentials.json"
echo "GOOGLE_APPLICATION_CREDENTIALS = " $GOOGLE_APPLICATION_CREDENTIALS
# source 沒有 export