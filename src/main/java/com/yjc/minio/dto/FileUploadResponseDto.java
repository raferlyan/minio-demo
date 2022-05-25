package com.yjc.minio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author raferlyan
 * @date 2022/5/25 11:25
 **/
@Getter
@Setter
@Builder
public class FileUploadResponseDto {

    private String bucketName;

    private List<String> fileName;
}
