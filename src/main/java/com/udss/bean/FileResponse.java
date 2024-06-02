package com.udss.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class FileResponse {
    private String fileName;
    private Date lastModified;

    public FileResponse (final S3Object s3Object) {
        this.fileName = s3Object.key();
        this.lastModified = Date.from(s3Object.lastModified());
    }
}
