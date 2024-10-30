package org.ivipa.ratel.rockie.domain.service;

import cn.hutool.core.io.IoUtil;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.ivipa.ratel.rockie.common.utils.RockieError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.minio.MinioClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MinioStorageService {

    @Value("${ratel.rockie.oss.address}")
    private String ossAddress;

    @Value("${ratel.rockie.oss.access-key}")
    private String ossAccessKey;

    @Value("${ratel.rockie.oss.secret-key}")
    private String ossSecurityKey;

    @Value("${ratel.rockie.oss.bucket}")
    private String ossBucket;

    @Value("${ratel.rockie.oss.call-timeout}")
    private int ossCallTimeout;

    @Value("${ratel.rockie.oss.read-timeout}")
    private int ossReadTimeout;

    private MinioClient minioClient;

    private OkHttpClient httpClient;

    @PostConstruct
    private void initializeMinio() {
        httpClient = new OkHttpClient().newBuilder()
                .retryOnConnectionFailure(true)
                .callTimeout(ossCallTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(ossReadTimeout, TimeUnit.MILLISECONDS)
                .build();
        minioClient = MinioClient.builder()
                .httpClient(httpClient)
                .endpoint(ossAddress)
                .credentials(ossAccessKey, ossSecurityKey)
                .build();
    }

    public void createObject(String name, String path, String customerCode, MultipartFile file) {
        try {
            String filePath = path == null ? customerCode + "/" + name : customerCode + "/" + path + "/" +  name;
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(ossBucket)
                            .object(filePath)
                            .stream(file.getInputStream(), -1, 10485760)
                            .build());
        } catch (Exception e) {
            throw RockieError.STORAGE_CREATE_DOCUMENT_EXCEPTION.newException();
        }
    }


    public void createObject(String name, String path, String customerCode, byte[] fileData) {
        try {
            String filePath = path == null ? customerCode + "/" + name : customerCode + "/" + path + "/" +  name;
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(ossBucket)
                            .object(filePath)
                            .build());
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(ossBucket)
                            .object(filePath)
                            .stream(new ByteArrayInputStream(fileData), fileData.length, -1)
                            .build());
        } catch (Exception e) {
            log.error("未知异常:cause:{},message:{},stackTrace:{}",e.getCause(),e.getMessage(),e.getStackTrace());
            throw RockieError.STORAGE_CREATE_DOCUMENT_EXCEPTION.newException();
        }
    }


    public void createObject(String name, String path, String customerCode, InputStream fileStream) {
        try {
            String filePath = path == null ? customerCode + "/" + name : customerCode + "/" + path + "/" +  name;
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(ossBucket)
                            .object(filePath)
                            .stream(fileStream, -1, 10485760)
                            .build());
        } catch (Exception e) {
            throw RockieError.STORAGE_CREATE_DOCUMENT_EXCEPTION.newException();
        }
    }

    public byte[] getObject(String name, String path, String customerCode) {
        try {
            String filePath = path == null ? customerCode + "/" + name : customerCode + "/" + path + "/" +  name;
            InputStream stream;
            stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(ossBucket)
                            .object(filePath)
                            .build());
            return IoUtil.readBytes(stream);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw RockieError.STORAGE_GET_DOCUMENT_EXCEPTION.newException();
        }
    }


    public InputStream getObjectStream(String name, String path, String customerCode) {
        try {
            String filePath = path == null ? customerCode + "/" + name : customerCode + "/" + path + "/" +  name;
            InputStream stream;
            stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(ossBucket)
                            .object(filePath)
                            .build());
            return stream;
        } catch (Exception e) {
            throw RockieError.STORAGE_CREATE_DOCUMENT_EXCEPTION.newException();
        }
    }

    public byte[] getDocumentSize(String name, String path, String customerCode) {
        return null;
    }
}
