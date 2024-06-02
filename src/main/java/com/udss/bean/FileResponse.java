package com.udss.bean;

import com.udss.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileResponse that = (FileResponse) o;
        return Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fileName);
    }
}
