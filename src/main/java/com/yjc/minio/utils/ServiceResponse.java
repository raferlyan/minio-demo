package com.yjc.minio.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author raferlyan
 * @date 2022/5/25 11:32
 **/
@Getter
@Setter
@NoArgsConstructor
public class ServiceResponse<T> {
    private int code = 200;

    private String message = "";

    private boolean messageVisible = false;

    private T data;

    public static ServiceResponse<Object> ok() {
        return new ServiceResponse<>();
    }

    public static <T> ServiceResponse<T> ok(T data) {
        ServiceResponse<T> serviceResponse = new ServiceResponse<T>();
        serviceResponse.setData(data);
        return serviceResponse;
    }

    public static <T> ServiceResponse<T> fail(String message) {
        ServiceResponse<T> serviceResponse = new ServiceResponse<T>();
        serviceResponse.setCode(400);
        serviceResponse.setMessage(message);
        return serviceResponse;
    }

    public static <T> ServiceResponse<T> ok(String message) {
        ServiceResponse<T> serviceResponse = new ServiceResponse<>();
        serviceResponse.setCode(200);
        serviceResponse.setMessage(message);
        return serviceResponse;
    }

    public static <T> ServiceResponse<T> ok(String message, T data) {
        ServiceResponse<T> serviceResponse = new ServiceResponse<>();
        serviceResponse.setCode(200);
        serviceResponse.setMessage(message);
        serviceResponse.setData(data);
        return serviceResponse;
    }
}
