package org.ivipa.ratel.rockie.domain.service;

import cn.hutool.core.io.IoUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.AccessControlList;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyuncs.auth.EnvironmentVariableCredentialsProvider;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.ivipa.ratel.rockie.common.utils.RockieError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AliyunOSSStorageService {

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

    private OSS ossClient;

    @PostConstruct
    private void initializeOSS() {
        DefaultCredentialProvider credentialsProvider;

        credentialsProvider = CredentialsProviderFactory.newDefaultCredentialProvider(ossAccessKey, ossSecurityKey);
        ossClient = new OSSClientBuilder().build(ossAddress, credentialsProvider);

        try {
            AccessControlList acl = ossClient.getBucketAcl(ossBucket);
            log.info(acl.toString());
        }catch (OSSException oe) {
            log.info("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.info("Error Message:" + oe.getErrorMessage());
            log.info("Error Code:" + oe.getErrorCode());
            log.info("Request ID:" + oe.getRequestId());
            log.info("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            log.info("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.info("Error Message:" + ce.getMessage());
        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
        }
    }

    public void createObject(String name, String path, String customerCode, MultipartFile file) {
        try {
        } catch (Exception e) {
            throw RockieError.STORAGE_CREATE_DOCUMENT_EXCEPTION.newException();
        }
    }


    public void createObject(String name, String path, String customerCode, byte[] fileData) {
        try {
            PutObjectRequest request;
            String filePath = path == null ? customerCode + "/" + name : customerCode + "/" + path + "/" +  name;
            boolean found = ossClient.doesObjectExist(ossBucket, filePath);
            if(found) {
                ossClient.deleteObject(ossBucket, filePath);
            }
            request =  new PutObjectRequest(ossBucket, filePath, new ByteArrayInputStream(fileData));
            ossClient.putObject(request);
        } catch (Exception e) {
            log.error("未知异常:cause:{},message:{},stackTrace:{}",e.getCause(),e.getMessage(),e.getStackTrace());
            throw RockieError.STORAGE_CREATE_DOCUMENT_EXCEPTION.newException();
        }
    }


    public void createObject(String name, String path, String customerCode, InputStream fileStream) {
        try {
            PutObjectRequest request;
            String filePath = path == null ? customerCode + "/" + name : customerCode + "/" + path + "/" +  name;
            request =  new PutObjectRequest(ossBucket, filePath, fileStream);
            ossClient.putObject(request);
        } catch (Exception e) {
            throw RockieError.STORAGE_CREATE_DOCUMENT_EXCEPTION.newException();
        }
    }

    public byte[] getObject(String name, String path, String customerCode) {
        InputStream inputStream;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            GetObjectRequest request;
            OSSObject ossObject;
            String filePath = path == null ? customerCode + "/" + name : customerCode + "/" + path + "/" +  name;
            request =  new GetObjectRequest(ossBucket, filePath);
            ossObject = ossClient.getObject(request);
            inputStream = ossObject.getObjectContent();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            inputStream.close();
            return byteArray;
        } catch (Exception e) {
            throw RockieError.STORAGE_CREATE_DOCUMENT_EXCEPTION.newException();
        }
    }


    public String getObjectText(String name, String path, String customerCode) {
        try {
            GetObjectRequest request;
            OSSObject ossObject;
            String filePath = path == null ? customerCode + "/" + name : customerCode + "/" + path + "/" +  name;
            request =  new GetObjectRequest(ossBucket, filePath);
            ossObject = ossClient.getObject(request);
            InputStream inputStream = ossObject.getObjectContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            throw RockieError.STORAGE_CREATE_DOCUMENT_EXCEPTION.newException();
        }
    }




    public InputStream getObjectStream(String name, String path, String customerCode) {
        try {
            InputStream stream = null;
//            stream = minioClient.getObject(
//                    GetObjectArgs.builder()
//                            .bucket(ossBucket)
//                            .object(customerCode + "/" + path + "/" +  name)
//                            .build());
            return stream;
        } catch (Exception e) {
            throw RockieError.STORAGE_CREATE_DOCUMENT_EXCEPTION.newException();
        }
    }

    public byte[] getDocumentSize(String name, String path, String customerCode) {
        return null;
    }
}
