package com.whoiszxl.logger.annotation;

import java.lang.annotation.*;

/**
 * 日志记录注解
 *
 * @author whoiszxl
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BananaLog {

    /**
     * 描述
     */
    String value();

    /**
     * 记录请求参数，默认记录
     * @return
     */
    boolean recordRequestParams() default true;

    /**
     * 记录返回参数，默认记录
     * @return
     */
    boolean recordResponseParams() default true;

    /**
     * 是否忽略日志记录（仅用于接口方法上）
     */
    boolean ignore() default false;
}

