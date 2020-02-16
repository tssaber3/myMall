package com.tssaber.mmall.exception;

import com.tssaber.mmall.common.CExceptionEnums;
import org.apache.commons.lang.StringUtils;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/11/25 0025 18:40
 */
public class GlobalException extends RuntimeException {

    private CExceptionEnums cExceptionEnums;

    private String errorMessage;

    /**
     * 自定义异常构造方法
     * @param cExceptionEnums 异常枚举对象
     * @param errorMessage 消息内容
     */
    public GlobalException(CExceptionEnums cExceptionEnums,String errorMessage){
        this.cExceptionEnums = cExceptionEnums;
        this.errorMessage = StringUtils.isNotBlank(errorMessage) ? errorMessage : cExceptionEnums.getErrorDesc();
    }

    public GlobalException(CExceptionEnums cExceptionEnums){
        this.cExceptionEnums = cExceptionEnums;
        this.errorMessage = cExceptionEnums.getErrorDesc();
    }

    public CExceptionEnums getcExceptionEnums() {
        return cExceptionEnums;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
