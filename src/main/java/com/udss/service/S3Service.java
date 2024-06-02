package com.udss.service;

import com.udss.bean.FileResponse;
import com.udss.exception.FileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service implements IS3Service {

    private final S3Client s3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public List<FileResponse> searchFiles(final String username, final String term) {
        try {
            final ListObjectsV2Request objRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(username + "/")
                    .build();
            final ListObjectsV2Response objResponse = s3Client.listObjectsV2(objRequest);

            return objResponse.contents().stream()
                    .filter(s3Object -> s3Object.key().contains(term))
                    .map(FileResponse::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new FileException("Error occurred while searching files: " + e);
        }
    }

    @Override
    public void uploadFile(final String username, final MultipartFile file) {
        final String key = username + "/" + file.getOriginalFilename();
        final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}

