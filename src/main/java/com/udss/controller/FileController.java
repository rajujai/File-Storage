package com.udss.controller;

import com.udss.bean.FileResponse;
import com.udss.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/files")
@RestController
public class FileController {

    private final S3Service s3Service;

    public FileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/search/{username}")
    public List<FileResponse> searchFiles(@PathVariable final String username, @RequestParam final String term) {
        return s3Service.searchFiles(username, term);
    }

    @PostMapping("/upload/{username}")
    public ResponseEntity<String> uploadFile(@PathVariable final String username, final MultipartFile file) {
        s3Service.uploadFile(username, file);
        return ResponseEntity.ok("File uploaded successfully.");
    }
}

