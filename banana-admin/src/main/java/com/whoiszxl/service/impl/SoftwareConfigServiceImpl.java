package com.whoiszxl.service.impl;

import com.whoiszxl.entity.SoftwareConfig;
import com.whoiszxl.mapper.SoftwareConfigMapper;
import com.whoiszxl.service.ISoftwareConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 基础组件配置表 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Service
public class SoftwareConfigServiceImpl extends ServiceImpl<SoftwareConfigMapper, SoftwareConfig> implements ISoftwareConfigService {

    @Override
    public SoftwareConfig getBySoftwareConfigName(String softwareConfigName) {
        return this.lambdaQuery().eq(SoftwareConfig::getConfigName, softwareConfigName).one();
    }
}
