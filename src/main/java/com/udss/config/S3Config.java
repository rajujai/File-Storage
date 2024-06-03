package com.udss.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

@Slf4j
@Configuration
public class S3Config {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${application.credentials.path}")
    private String credentialsPath;

    @Value("${aws.region}")
    private String region;

    @Bean
    public AwsBasicCredentials awsBasicCredentials() {
        final S3Client s3Client = S3Client.builder()
                .region(Region.of(region))
                .build();

        final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(credentialsPath)
                .build();

        try {
            final ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
            final byte[] encryptedCredentials = objectBytes.asByteArray();
            final byte[] decodedCredentials = Base64.getDecoder().decode(encryptedCredentials);

            final KmsClient kmsClient = KmsClient.builder()
                    .region(Region.of(region))
                    .build();

            final DecryptRequest decryptRequest = DecryptRequest.builder()
                    .ciphertextBlob(SdkBytes.fromByteArray(decodedCredentials))
                    .build();

            final DecryptResponse decryptResponse = kmsClient.decrypt(decryptRequest);
            final byte[] decryptedBytes = decryptResponse.plaintext().asByteArray();
            final String decryptedContent = new String(decryptedBytes, StandardCharsets.UTF_8);

            final Properties properties = new Properties();
            properties.load(new ByteArrayInputStream(decryptedContent.getBytes(StandardCharsets.UTF_8)));
            log.info("AWS credentials acquired successfully");
            return AwsBasicCredentials.create(
                    properties.getProperty("aws.accessKeyId"),
                    properties.getProperty("aws.secretKey")
            );
        } catch (S3Exception e) {
            log.error("Error retrieving credentials from S3: {}", e.getMessage(), e);
            throw new RuntimeException("Error retrieving credentials from S3: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error decrypting credentials S3: {}", e.getMessage(), e);
            throw new RuntimeException("Error decrypting credentials: " + e.getMessage(), e);
        }
    }

    @Bean
    public S3Client s3Client(final AwsBasicCredentials awsBasicCredentials) {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }
}

