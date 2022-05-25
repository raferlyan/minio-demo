package com.yjc.minio.demo;

import io.minio.DownloadObjectArgs;
import io.minio.MinioClient;

/**
 * @author raferlyan
 * @date 2022/5/25 09:04
 **/
public class FileDownload {
    public static void main(String[] args) {
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://ip:9000")
                .credentials("ADMIN","AdminAdmin")
                .build();

        try {
            minioClient.downloadObject(DownloadObjectArgs.builder().bucket("bucketName").object("image.jpg").filename("/Path/to/download.jpg").build());
            System.out.println("download successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
