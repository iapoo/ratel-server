# Setup
docker run --name mysql -p 3307:3306  -e MYSQL_ROOT_PASSWORD=password -d mysql:5.7
docker run --name redis -p 6380:6379 -d redis
