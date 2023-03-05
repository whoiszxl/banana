package com.whoiszxl.core.exception;

import com.whoiszxl.core.exception.custom.ServiceException;

/**
 * 异常捕获工具
 * @author whoiszxl
 */
public class ExceptionCatcher {

    public static void catchServiceEx(String message) {
        throw new ServiceException(message);
    }
}
