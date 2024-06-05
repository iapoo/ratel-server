# Readme

## Setup

docker run --name mysql -p 3307:3306  -e MYSQL_ROOT_PASSWORD=password -d mysql:5.7
docker run --name redis -p 6380:6379 -d redis
docker run -p 9000:9000 -p 9090:9090 --name minio -v ~/data/minio:/data \
    -e "MINIO_ROOT_USER=root" \
    -e "MINIO_ROOT_PASSWORD=password" \
    quay.io/minio/minio server /data --console-address ":9090"

## Build

    mvn package -DskipTests

## Test & Run

    java -jar ratel-system-server/target/ratel-system-server-1.0.0.jar
