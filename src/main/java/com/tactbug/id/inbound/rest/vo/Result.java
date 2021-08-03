package com.tactbug.id.inbound.rest.vo;

import lombok.Data;

@Data
public class Result<T> {

    private Integer code;
    private String info;
    private Boolean success;
    private T data;

    public static <T> Result<T> success(){
        Result<T> result = new Result<>();
        result.setCode(10000);
        result.setSuccess(true);
        result.setInfo("成功");
        return result;
    }

    public static <T> Result<T> success(T t){
        Result<T> result = new Result<>();
        result.setCode(10000);
        result.setSuccess(true);
        result.setInfo("成功");
        result.setData(t);
        return result;
    }

    public static <T> Result<T> fail(){
        Result<T> result = new Result<>();
        result.setCode(9999);
        result.setInfo("系统异常");
        result.setSuccess(false);
        return result;
    }
}
