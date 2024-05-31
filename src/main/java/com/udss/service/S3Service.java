package com.udss.service;

import com.udss.bean.FileResponse;
import com.udss.exception.FileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public List<FileResponse> searchFiles(String username, String term) {
        try {
            ListObjectsV2Request objRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(username + "/")
                    .build();

            ListObjectsV2Response objResponse = s3Client.listObjectsV2(objRequest);

            return objResponse.contents().stream()
                    .filter(s3Object -> s3Object.key().contains(term))
                    .map(s3Object -> new FileResponse(s3Object.key(), getBucketUrl(s3Object.key())))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new FileException("Error occurred while searching files", e);
        }
    }

    private String getBucketUrl(String key) {
        return "https://s3.amazonaws.com/" + bucketName + "/" + key;
    }
}

