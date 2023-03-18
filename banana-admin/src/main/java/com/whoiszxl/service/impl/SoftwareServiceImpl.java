package com.whoiszxl.service.impl;

import com.whoiszxl.entity.Software;
import com.whoiszxl.mapper.SoftwareMapper;
import com.whoiszxl.service.ISoftwareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 基础组件表 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Service
public class SoftwareServiceImpl extends ServiceImpl<SoftwareMapper, Software> implements ISoftwareService {

}
