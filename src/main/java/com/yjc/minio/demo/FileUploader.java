package com.yjc.minio.demo;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author raferlyan
 * @date 2022/5/25 08:45
 **/
public class FileUploader {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint("http://ip:9000")
                    .credentials("ADMIN","AdminAdmin")
                    .build();

            String bucketName = "bucketName";
               boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
                if (!bucketExists){
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                }

                minioClient.uploadObject(UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object("image.jpg")
                        .filename("/Path/to/download/image.JPG") //本地磁盘的路径
                        .build());

            System.out.println("上传成功！");
        } catch (MinioException e) {
            e.printStackTrace();
        }
    }
}
