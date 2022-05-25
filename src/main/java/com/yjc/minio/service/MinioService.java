package com.yjc.minio.service;

import com.yjc.minio.dto.FileDto;
import com.yjc.minio.dto.FileUploadResponseDto;
import com.yjc.minio.utils.ServiceResponse;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raferlyan
 * @date 2022/5/25 09:25
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    @Value("${minio.bucketName}")
    private String bucket;

    private final MinioClient minioClient;

    public ServiceResponse<List<FileDto>> list() throws Exception {
        Iterable<Result<Item>> listObjects = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).build());
        System.out.println(listObjects);
        List<FileDto> fileDtoList = new ArrayList<>();
        for (Result<Item> listObject : listObjects) {
            String filename = listObject.get().objectName();
            Long size = listObject.get().size();
            System.out.println("===========================");
            FileDto fileDto = FileDto.builder().fileName(filename).size(size).build();
            fileDtoList.add(fileDto);
        }
        return ServiceResponse.ok(fileDtoList);
    }

    public ServiceResponse<FileUploadResponseDto> upload(MultipartFile[] file){
        List<String> originalFilenameList = new ArrayList<>(file.length);
        for (MultipartFile multipartFile : file) {
            String originalFilename = multipartFile.getOriginalFilename();
            originalFilenameList.add(originalFilename);
            try {
                InputStream inputStream = multipartFile.getInputStream();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(originalFilename)
                        .stream(inputStream,multipartFile.getSize(),-1)
                        .contentType(multipartFile.getContentType())
                        .build());
                inputStream.close();
            } catch (Exception e){
                log.error(e.getMessage());
                return ServiceResponse.fail("上传失败");
            }
        }
        FileUploadResponseDto response = FileUploadResponseDto.builder().bucketName(bucket).fileName(originalFilenameList).build();
        return ServiceResponse.ok(response);
    }

    public ServiceResponse<Object> download(HttpServletResponse response, String fileName) {
        InputStream inputStream = null;
        try {
            StatObjectResponse statObject = minioClient.statObject(StatObjectArgs.builder().bucket(bucket).object(fileName).build());
            response.setContentType(statObject.contentType());
            response.setHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(fileName).build());
            IOUtils.copy(inputStream,response.getOutputStream());
        } catch (Exception e){
            log.error(e.getMessage());
        } finally {
          if (inputStream != null){
              try {
                  inputStream.close();
              } catch (IOException e){
                  log.error(e.getMessage());
              }
          }
        }
        return ServiceResponse.ok("下载成功");
    }

    public ServiceResponse<Object> remove(String fileName){
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(fileName).build());
        } catch (Exception e){
            log.error(e.getMessage());
            return ServiceResponse.fail("删除失败！");
        }
        return ServiceResponse.ok("删除成功！");
    }
}
