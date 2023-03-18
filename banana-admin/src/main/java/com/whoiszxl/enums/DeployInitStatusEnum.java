package com.whoiszxl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 部署初始化状态枚举
 * @author whoiszxl
 */
@Getter
@AllArgsConstructor
public enum DeployInitStatusEnum {

    /**
     * 已初始化
     */
    INITIALIZED(1),

    /**
     * 未初始化
     */
    NOT_INIT( 0),
    ;

    private final Integer code;
}