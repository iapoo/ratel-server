package org.ivipi.ratel.rockie.domain.service;

import cn.hutool.core.io.IoUtil;
import io.minio.GetObjectArgs;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.ivipi.ratel.rockie.common.utils.RockieError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StorageService {

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
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(ossBucket)
                            .object(customerCode + "/" + path + "/" +  name)
                            .stream(file.getInputStream(), -1, 10485760)
                            .build());
        } catch (Exception e) {
            throw RockieError.STORAGE_CREATE_DOCUMENT_EXCEPTION.newException();
        }
    }


    public void createObject(String name, String path, String customerCode, byte[] fileData) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(ossBucket)
                            .object(customerCode + "/" + path + "/" +  name)
                            .stream(new ByteArrayInputStream(fileData), fileData.length, -1)
                            .build());
        } catch (Exception e) {
            log.error("未知异常:cause:{},message:{},stackTrace:{}",e.getCause(),e.getMessage(),e.getStackTrace());
            throw RockieError.STORAGE_CREATE_DOCUMENT_EXCEPTION.newException();
        }
    }


    public void createObject(String name, String path, String customerCode, InputStream fileStream) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(ossBucket)
                            .object(customerCode + "/" + path + "/" +  name)
                            .stream(fileStream, -1, 10485760)
                            .build());
        } catch (Exception e) {
            throw RockieError.STORAGE_CREATE_DOCUMENT_EXCEPTION.newException();
        }
    }

    public byte[] getObject(String name, String path, String customerCode) {
        try {
            InputStream stream;
            stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(ossBucket)
                            .object(customerCode + "/" + path + "/" +  name)
                            .build());
            return IoUtil.readBytes(stream);
        } catch (Exception e) {
            throw RockieError.STORAGE_CREATE_DOCUMENT_EXCEPTION.newException();
        }
    }


    public InputStream getObjectStream(String name, String path, String customerCode) {
        try {
            InputStream stream;
            stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(ossBucket)
                            .object(customerCode + "/" + path + "/" +  name)
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
