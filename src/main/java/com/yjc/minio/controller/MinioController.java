package com.yjc.minio.controller;

import com.yjc.minio.dto.FileDto;
import com.yjc.minio.dto.FileUploadResponseDto;
import com.yjc.minio.service.MinioService;
import com.yjc.minio.utils.ServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author raferlyan
 * @date 2022/5/25 09:22
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
public class MinioController {

    private final MinioService minioService;

    @GetMapping("/list")
    public ServiceResponse<List<FileDto>> list() throws Exception {
        return minioService.list();
    }

    @PostMapping("/upload")
    public ServiceResponse<FileUploadResponseDto> download(@RequestPart MultipartFile[] file) {
        if (file == null || file.length == 0){
            return ServiceResponse.fail("上传失败！");
        }
        return minioService.upload(file);
    }

    @PostMapping("/download")
    public ServiceResponse<Object> upload(HttpServletResponse response, @RequestParam String fileName) {
        return minioService.download(response, fileName);
    }

    @DeleteMapping("/remove")
    public ServiceResponse<Object> remove(@RequestParam String fileName){
        return minioService.remove(fileName);
    }
}
