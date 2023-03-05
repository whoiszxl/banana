package com.whoiszxl.core.properties;

import cn.hutool.extra.spring.SpringUtil;

/**
 * rsa配置
 * @author whoiszxl
 */
public class RsaProperties {

    public static final String PRIVATE_KEY;

    static {
        PRIVATE_KEY = SpringUtil.getProperty("banana.rsa.privateKey");
    }
}
