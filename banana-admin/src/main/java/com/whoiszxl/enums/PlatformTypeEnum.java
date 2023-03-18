package com.whoiszxl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志状态类型
 * @author whoiszxl
 */
@Getter
@AllArgsConstructor
public enum PlatformTypeEnum {

    /**
     * 成功
     */
    ALIYUN("aliyunServerGenerateStrategy", 2),

    /**
     * 失败
     */
    QCLOUD("qcloudServerGenerateStrategy", 3),
    ;

    private static Map<Integer,PlatformTypeEnum> cache = new HashMap<>();
    static {
        for (PlatformTypeEnum value : PlatformTypeEnum.values()) {
            cache.put(value.getCode(),value);
        }
    }

    public static PlatformTypeEnum find(Integer code){
        return cache.get(code);
    }

    private final String name;
    private final Integer code;
}