#!/bin/bash

if [ "$1" = "dev" ]
then
  git pull
  docker-compose down
  docker image prune -f
  docker volume prune -f
  ./gradlew clean bootJar
  docker-compose build --no-cache
  docker-compose up -d

elif [ "$1" = "prod" ]
then
  git pull
  docker-compose -f docker-compose.prod.yml down
  docker image prune -f
  docker volume prune -f
  ./gradlew clean bootJar
  docker-compose -f docker-compose.prod.yml build --no-cache
  docker-compose -f docker-compose.prod.yml up -d

else
  echo "please send environment variable"

fi
