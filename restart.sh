#!/bin/bash

if [ "$1" = "dev" ]
then
  git pull
  ./gradlew :$2:clean :$2:build
  docker-compose build --no-cache $2 && docker-compose up -d $2

elif [ "$1" = "prod" ]
then
  git pull
  ./gradlew :$2:clean :$2:build
  docker-compose -f docker-compose.prod.yml build --no-cache $2 && docker-compose -f docker-compose.prod.yml up -d $2

else
  echo "please send environment variable"

fi
