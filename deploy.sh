#!/bin/bash

docker-compose pull redis
docker-compose up -d redis

IS_GREEN=$(docker ps | grep green) # 현재 실행중인 App이 blue인지 확인합니다.

if [ -z "$IS_GREEN" ]; then # green이 없으면 blue라면

  echo "### BLUE => GREEN ###"

  echo "1. get green image"
  docker-compose pull green # green으로 이미지를 내려받습니다.

  echo "2. green container up"
  docker-compose up -d green # green 컨테이너 실행

  for cnt in {1..20}
  do
    echo "3. green health check..."
    echo "서버 응답 확인중(${cnt}/20)";

    REQUEST=$(curl http://127.0.0.1:8080) # green으로 request
    if [ -n "$REQUEST" ]
    then # 서비스 가능하면 health check 중지
      echo "health check success"
      break ;
    else
      sleep 10
    fi
  done;

  if [ $cnt -eq 20 ]
  then
    echo "서버가 정상적으로 구동되지 않았습니다."
    exit 1
  fi

  echo "4. reload nginx"
  sudo -n cp /etc/nginx/nginx.green.conf /etc/nginx/nginx.conf
  sudo -n nginx -s reload

  echo "5. blue container down"
  docker-compose stop blue
else
  echo "### GREEN => BLUE ###"

  echo "1. get blue image"
  docker-compose pull blue

  echo "2. blue container up"
  docker-compose up -d blue

  for cnt in {1..20}
  do
    echo "3. blue health check..."
    echo "서버 응답 확인중(${cnt}/20)";

    REQUEST=$(curl http://127.0.0.1:8081) # blue로 request

    if [ -n "$REQUEST" ]
    then # 서비스 가능하면 health check 중지
      echo "health check success"
      break ;
    else
      sleep 10
    fi
  done;

  if [ $cnt -eq 20 ]
  then
    echo "서버가 정상적으로 구동되지 않았습니다."
    exit 1
  fi

  echo "4. reload nginx"
  sudo -n cp /etc/nginx/nginx.blue.conf /etc/nginx/nginx.conf
  sudo -n nginx -s reload

  echo "5. green container down"
  docker-compose stop green
fi
