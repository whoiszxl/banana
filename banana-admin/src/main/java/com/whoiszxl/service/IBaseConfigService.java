package com.whoiszxl.service;

import com.whoiszxl.entity.BaseConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 基础配置表 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
public interface IBaseConfigService extends IService<BaseConfig> {

    /**
     * 通过key获取配置
     * @param key 键
     * @return 值
     */
    String getByKey(String key);
}
