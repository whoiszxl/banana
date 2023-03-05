package com.whoiszxl.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.whoiszxl.core.constants.CommonConstants;
import com.whoiszxl.service.IMenuService;
import com.whoiszxl.service.IRoleService;
import com.whoiszxl.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 权限服务接口实现
 * @author whoiszxl
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IRoleService roleService;


    @Override
    public Set<String> listPermissionsByAdminId(Integer adminId) {
        Set<String> roleList = listRoleByAdminId(adminId);
        if(roleList.contains(CommonConstants.SUPER_ADMIN)) {
            return CollUtil.newHashSet(CommonConstants.ALL_PERMISSION);
        }
        return menuService.listPermissionsByAdminId(adminId);
    }

    @Override
    public Set<String> listRoleByAdminId(Integer adminId) {
        return roleService.listRoleByAdminId(adminId);
    }
}
