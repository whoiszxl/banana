package com.whoiszxl.strategy;


import com.whoiszxl.enums.PlatformTypeEnum;

/**
 * @author whoiszxl
 */
public interface ServerGenerateStrategyFactory {

    /**
     * 获取策略类
     * @param platformName 平台类型
     * @return 策略类
     */
    ServerGenerateStrategy getStrategy(String platformName);
}
