package com.udss.service;

import com.udss.bean.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IS3Service {
    List<FileResponse> searchFiles(final String username, final String term);
    void uploadFile(final String userName, final MultipartFile file);
}
