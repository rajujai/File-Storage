package com.udss.bean;

import com.udss.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.services.s3.model.S3Object;

@Getter
@Setter
@AllArgsConstructor
public class FileResponse {
    private String fileName;
    private String lastModified;

    public FileResponse (final S3Object s3Object) {
        this.fileName = s3Object.key();
        this.lastModified = DateUtils.format(s3Object.lastModified(), DateUtils.format1);
    }
}
