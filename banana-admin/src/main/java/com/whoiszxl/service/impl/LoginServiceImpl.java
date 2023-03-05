package com.whoiszxl.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.whoiszxl.core.constants.CommonConstants;
import com.whoiszxl.core.token.TokenHelper;
import com.whoiszxl.core.token.entity.LoginMember;
import com.whoiszxl.core.utils.SecureUtils;
import com.whoiszxl.entity.Admin;
import com.whoiszxl.service.IAdminService;
import com.whoiszxl.service.LoginService;
import com.whoiszxl.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author whoiszxl
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private PermissionService permissionService;

    @Override
    public String login(String username, String password) {
        //1. 校验管理员是否存在
        Admin admin = adminService.getOne(Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getUsername, username));
        Assert.notNull(admin, "管理员不存在");

        //2. 校验密码是否正确
        if(!SecureUtils.passwordMd5(password, CommonConstants.PASSWORD_SALT).equals(admin.getPassword())) {
            throw new RuntimeException();
        }

        //3. 进行登录，并设置权限
        LoginMember loginMember = BeanUtil.copyProperties(admin, LoginMember.class);
        loginMember.setPermissions(permissionService.listPermissionsByAdminId(admin.getId()));
        loginMember.setRoles(permissionService.listRoleByAdminId(admin.getId()));
        tokenHelper.login(loginMember);
        return StpUtil.getTokenValue();
    }
}
