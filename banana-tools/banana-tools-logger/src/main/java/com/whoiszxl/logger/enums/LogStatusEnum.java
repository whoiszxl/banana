package com.whoiszxl.logger.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 日志状态类型
 * @author whoiszxl
 */
@Getter
@AllArgsConstructor
public enum LogStatusEnum {

    /**
     * 成功
     */
    SUCCESS(1, "success"),

    /**
     * 失败
     */
    FAIL(2, "失败"),
    ;
    private final Integer code;
    private final String desc;
}