package com.yjc.minio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author raferlyan
 * @date 2022/5/25 09:40
 **/
@Getter
@Setter
@Builder
public class FileDto {
    private String fileName;

    private Long size;
}
