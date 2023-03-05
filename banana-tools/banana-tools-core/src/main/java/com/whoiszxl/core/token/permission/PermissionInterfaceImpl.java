package com.whoiszxl.core.token.permission;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import com.whoiszxl.core.token.TokenHelper;
import com.whoiszxl.core.token.entity.LoginMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * sa-token 权限认证机制接口实现
 *
 * @author whoiszxl
 * @date 2021/7/20
 */
@Component
public class PermissionInterfaceImpl implements StpInterface {

    @Autowired
    private TokenHelper tokenHelper;

    /**
     * 返回一个账号所拥有的权限集合
     * @param loginId 用户登录标识
     * @param loginType 登录类型
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginMember loginMember = tokenHelper.getLoginMember();
        return new ArrayList<>(loginMember.getPermissions());
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     * @param loginId 用户登录标识
     * @param loginType 登录类型
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginMember loginMember = tokenHelper.getLoginMember();
        return new ArrayList<>(loginMember.getRoles());
    }




}
