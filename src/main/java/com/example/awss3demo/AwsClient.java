package com.example.awss3demo;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import java.io.IOException;
import java.io.InputStream;

public class AwsClient {
    public static AmazonS3 initClient(String serverUrl, String accessKey, String secretKey) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        ClientConfiguration config = new ClientConfiguration();
        config.setProtocol(Protocol.HTTP);
        config.setConnectionTimeout(30000);
        config.setUseExpectContinue(true);
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(serverUrl, "");
        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(config)
                .withEndpointConfiguration(endpointConfiguration)
                .withPathStyleAccessEnabled(true).build();
    }

    public static InputStream getOssFile(String bucketName, String ossKey, String serverUrl, String accessKey, String secretKey) {
        AmazonS3 amazonS3 = initClient(serverUrl, accessKey, secretKey);
        S3Object object = amazonS3.getObject(bucketName, ossKey);
        return object.getObjectContent();
    }

    public static void removeOssFile(String bucketName, String ossKey, String serverUrl, String accessKey, String secretKey) {
        AmazonS3 amazonS3 = initClient(serverUrl, accessKey, secretKey);
        amazonS3.deleteObject(bucketName, ossKey);
    }

    public static void uploadOssFile(InputStream inputStream, String ossKey, String bucketName, String serverUrl, String accessKey, String secretKey) throws IOException {
        AmazonS3 amazonS3 = initClient(serverUrl, accessKey, secretKey);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(inputStream.available());
        PutObjectResult putObjectResult = amazonS3.putObject(bucketName, ossKey, inputStream, objectMetadata);
    }
}
