package com.whoiszxl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whoiszxl.core.constants.CommonConstants;
import com.whoiszxl.core.utils.SecureUtils;
import com.whoiszxl.cqrs.command.AdminAddCommand;
import com.whoiszxl.cqrs.command.AdminRoleUpdateCommand;
import com.whoiszxl.entity.Admin;
import com.whoiszxl.mapper.AdminMapper;
import com.whoiszxl.service.IAdminRoleService;
import com.whoiszxl.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-02
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private IAdminRoleService adminRoleService;

    @Autowired
    private AdminMapper adminMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addAdmin(AdminAddCommand command) {
        //1. 校验此用户是否存在
        boolean exists = this.lambdaQuery().eq(Admin::getUsername, command.getUsername()).exists();
        Assert.isFalse(exists, "用户已存在");

        //2. 新增
        Admin admin = BeanUtil.copyProperties(command, Admin.class);
        this.save(admin);

        //3. 密码设置
        this.lambdaUpdate()
                .set(Admin::getPassword,
                        SecureUtils.passwordMd5(command.getPassword(), CommonConstants.PASSWORD_SALT));

        //4. 角色设置
        adminRoleService.save(command.getRoleIds(), admin.getId());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAdminRole(AdminRoleUpdateCommand command) {
        Admin admin = adminMapper.selectById(command.getAdminId());
        Assert.notNull(admin, "管理员不存在");

        adminRoleService.resetRole(command.getRoleIds(), command.getAdminId());
        return null;
    }
}
