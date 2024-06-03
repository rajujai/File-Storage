package com.udss.controller;

import com.udss.bean.FileResponse;
import com.udss.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequestMapping("/files")
@RestController
public class FileController {

    private final S3Service s3Service;

    public FileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/search/{username}")
    public List<FileResponse> searchFiles(@PathVariable final String username, @RequestParam final String term) {
        log.info("Request for file search for user: {}, search term: {}", username, term);
        return s3Service.searchFiles(username, term);
    }

    @PostMapping("/upload/{username}")
    public ResponseEntity<String> uploadFile(@PathVariable final String username, final MultipartFile file) {
        log.info("Request for file upload to: {}", username);
        s3Service.uploadFile(username, file);
        log.info("Upload file to {} successful", username);
        return ResponseEntity.ok("File uploaded successfully.");
    }
}

