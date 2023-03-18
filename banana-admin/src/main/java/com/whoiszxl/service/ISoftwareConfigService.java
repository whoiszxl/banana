package com.whoiszxl.service;

import com.whoiszxl.entity.SoftwareConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 基础组件配置表 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
public interface ISoftwareConfigService extends IService<SoftwareConfig> {

    /**
     * 通过组件配置文件名或者组件配置相关信息
     * @param softwareConfigName 配置文件名
     * @return 组件配置文件信息
     */
    SoftwareConfig getBySoftwareConfigName(String softwareConfigName);
}
