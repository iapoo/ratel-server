docker run \
-p 9000:9000 \
-p 9090:9090 \
--name minio \
-v ~/data/minio:/data \
-e "MINIO_ROOT_USER=root" \
-e "MINIO_ROOT_PASSWORD=password" \
quay.io/minio/minio server /data --console-address ":9090"