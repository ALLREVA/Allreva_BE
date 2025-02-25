#!/bin/bash

docker-compose pull redis
docker-compose up -d redis

IS_GREEN=$(docker ps --filter "name=green" --filter "status=running" -q) # 실행 중인 green 확인

if [ -z "$IS_GREEN" ]; then
    echo "### BLUE => GREEN ###"

    echo "1. Pull green image"
    docker-compose pull green

    echo "2. Start green container"
    docker-compose up -d green

    for cnt in {1..20}; do
        echo "3. Green health check... (${cnt}/20)"
        REQUEST=$(curl -s http://localhost:8080)
        if [ -n "$REQUEST" ]; then
            echo "Health check success"
            break
        else
            sleep 10
        fi
    done

    if [ "$cnt" -eq 20 ]; then
        echo "Green 서버가 정상적으로 구동되지 않았습니다."
        exit 1
    fi

    echo "4. Update Nginx to route to green"
    docker cp ./nginx.green.conf nginx:/etc/nginx/nginx.conf
    docker exec nginx nginx -s reload

    echo "5. Stop blue container"
    docker-compose stop blue

else
    echo "### GREEN => BLUE ###"

    echo "1. Pull blue image"
    docker-compose pull blue

    echo "2. Start blue container"
    docker-compose up -d blue

    for cnt in {1..20}; do
        echo "3. Blue health check... (${cnt}/20)"
        REQUEST=$(curl -s http://localhost:8081)
        if [ -n "$REQUEST" ]; then
            echo "Health check success"
            break
        else
            sleep 10
        fi
    done

    if [ "$cnt" -eq 20 ]; then
        echo "Blue 서버가 정상적으로 구동되지 않았습니다."
        exit 1
    fi

    echo "4. Update Nginx to route to blue"
    docker cp ./nginx.blue.conf nginx:/etc/nginx/nginx.conf
    docker exec nginx nginx -s reload

    echo "5. Stop green container"
    docker-compose stop green
fi