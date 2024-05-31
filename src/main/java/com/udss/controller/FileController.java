package com.udss.controller;

import com.udss.bean.FileResponse;
import com.udss.service.S3Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileController {

    private final S3Service s3Service;

    public FileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/users/{username}/files/search")
    public List<FileResponse> searchFiles(@PathVariable String username, @RequestParam String term) {
        return s3Service.searchFiles(username, term);
    }
}

