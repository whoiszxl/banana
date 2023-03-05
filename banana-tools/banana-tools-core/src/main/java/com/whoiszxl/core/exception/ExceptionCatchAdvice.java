package com.whoiszxl.core.exception;

import com.whoiszxl.core.entity.ResponseResult;
import com.whoiszxl.core.exception.custom.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author whoiszxl
 */
@Slf4j
@ControllerAdvice
public class ExceptionCatchAdvice {


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseResult<String> catchException(Exception e, HttpServletRequest request) {
        log.error("ExceptionCatchAdvice|发生未知异常|{}", request.getRequestURL(), e);
        return ResponseResult.buildError(e.getMessage());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult<String> catchHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return ResponseResult.buildError(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceException.class)
    public ResponseResult<String> catchServiceException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return ResponseResult.buildError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
