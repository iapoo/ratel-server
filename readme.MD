# Ratel Server

## About Ratel Server

Ratel Server is backend of Ratel. Ratel is general diagram editor. There are 3 sub projects: Ratel-Web(frontend), Ratel-Server(backend) and Ratel-deployment(deployment)

## Try

### Try online demo (in progress)

Try onelin demo.

### Try all-in-one docker

Pleae check Ratel-Deployment

## Build

It requires JDK 21, Spring Boot 3.3.x and Maven 3.9.x

Build: mvn package -DskipTests

## Run

It requires Mysql & Redis & Minio. Here is sample docker command to setup environment.

### Prepare Mysql

docker run --name mysql -p 3307:3306  -e MYSQL_ROOT_PASSWORD=password -d mysql:5.7

### Prepare Redis

docker run --name redis -p 6380:6379 -d redis

### Prepare minio

docker run -p 9000:9000 -p 9090:9090 --name minio -v ~/data/minio:/data \
    -e "MINIO_ROOT_USER=root" \
    -e "MINIO_ROOT_PASSWORD=password" \
    quay.io/minio/minio server /data --console-address ":9090"

### Run Application

    java -jar ratel-system-server/target/ratel-system-server-1.0.0.jar
