package com.whoiszxl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whoiszxl.logger.entity.AdminOptLog;
import com.whoiszxl.logger.mapper.AdminOptLogMapper;
import com.whoiszxl.service.IAdminOptLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-02
 */
@Service
public class AdminOptLogServiceImpl extends ServiceImpl<AdminOptLogMapper, AdminOptLog> implements IAdminOptLogService {

}
