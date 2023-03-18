package com.whoiszxl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whoiszxl.entity.BaseConfig;
import com.whoiszxl.mapper.BaseConfigMapper;
import com.whoiszxl.service.IBaseConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 基础配置表 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Service
public class BaseConfigServiceImpl extends ServiceImpl<BaseConfigMapper, BaseConfig> implements IBaseConfigService {

    @Override
    public String getByKey(String configKey) {
        LambdaQueryWrapper<BaseConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseConfig::getConfigKey, configKey);
        BaseConfig config = this.getOne(queryWrapper);
        return config.getConfigValue();
    }
}
