package com.tssaber.mmall.common;

import com.tssaber.mmall.exception.GlobalException;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/11/25 0025 18:52
 */
public class ApiResponse<T> {

    private String code;
    private String msg;
    private T data;

    private ApiResponse(CExceptionEnums e){
        if (e != null){
            this.code = e.getErrorCode();
            this.msg = e.getErrorDesc();
        }
    }

    private ApiResponse(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    private ApiResponse(String code,String msg,T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<T>("200","成功",data);
    }

    public static <T> ApiResponse<T> error(CExceptionEnums e){
        return new ApiResponse<T>(e);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
