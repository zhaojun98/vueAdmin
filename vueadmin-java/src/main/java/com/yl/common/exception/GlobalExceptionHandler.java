package com.yl.common.exception;


import com.szkj.common.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * @Author GoryLee
 * @Date 2022/11/15 20:26
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResultVo handler(CustomException customException) {
        log.error("运行时异常--------->:", customException);
        Throwable cause = customException.getCause();
        return CommonResultVo.failed(cause.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResultVo handler(MethodArgumentNotValidException e) {
        log.error("校验异常--------->:", e);
        ObjectError objectError = e.getBindingResult().getAllErrors().stream().findFirst().orElse(null);
        return CommonResultVo.failed(objectError.getDefaultMessage());
    }

    /**
     * IllegalArgumentException异常处理返回json
     * 返回状态码:400
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResultVo handler(IllegalArgumentException e) {
        log.error("Assert异常--------->", e);
        return CommonResultVo.failed(e.getMessage());
    }

    /**
     * 返回状态码:405
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public CommonResultVo handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return CommonResultVo.failed("不支持当前请求方法:"+e.getMessage());
    }

    /**
     * 返回状态码:415
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public CommonResultVo handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return CommonResultVo.failed("不支持当前媒体类型:"+e.getMessage());
    }

    /**
     * SQLException sql异常处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class})
    public CommonResultVo handleSQLException(SQLException e) {
        return CommonResultVo.failed("服务内部错误:"+e.getMessage());
    }

    /**
     * 所有异常统一处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonResultVo handleException(Exception e) {
        return  CommonResultVo.failed("未知异常:"+ e.getMessage());
    }
}
