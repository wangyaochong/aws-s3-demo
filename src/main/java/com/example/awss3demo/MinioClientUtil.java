package com.example.awss3demo;

import io.minio.*;
import io.minio.errors.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinioClientUtil {

    public static MinioClient minioClient;

    public static MinioClient initMinio(String minioUrl, String minioName, String minioPassword) {
        minioClient = MinioClient.builder().endpoint(minioUrl).credentials(minioName, minioPassword).build();
        return minioClient;
    }

    public static InputStream getOssFile(String bucketName, String objectName, String url, String ak, String sk) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        InputStream inputStream = null;
        initMinio(url, ak, sk);
        GetObjectArgs args = GetObjectArgs.builder().object(objectName).bucket(bucketName).build();
        inputStream = minioClient.getObject(args);
        return inputStream;
    }

    public static void removeObject(String bucketName, String objectName, String url, String ak, String sk) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        initMinio(url, ak, sk);
        RemoveObjectArgs args = RemoveObjectArgs.builder().object(objectName).bucket(bucketName).build();
        minioClient.removeObject(args);
    }

    public static String upload(InputStream stream, String relativePath, String bucketName, String url, String ak, String sk) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        initMinio(url, ak, sk);
        if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            System.out.println("已经存在bucket");
        } else {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            System.out.println("创建新的bucket");
        }
        PutObjectArgs args = PutObjectArgs.builder().object(relativePath).bucket(bucketName).contentType("application/octet-stream").stream(stream, stream.available(), -1).build();
        minioClient.putObject(args);
        stream.close();
        return bucketName + "/" + relativePath;
    }
}
