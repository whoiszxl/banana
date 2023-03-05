package com.whoiszxl.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whoiszxl.entity.AdminRole;
import com.whoiszxl.entity.Role;
import com.whoiszxl.mapper.RoleMapper;
import com.whoiszxl.service.IAdminRoleService;
import com.whoiszxl.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-02
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IAdminRoleService adminRoleService;


    @Override
    public Set<String> listRoleByAdminId(Integer adminId) {
        List<AdminRole> adminRoleList = adminRoleService.list(Wrappers
                .<AdminRole>lambdaQuery().eq(AdminRole::getAdminId, adminId));
        List<Integer> roleIdList = adminRoleList.stream().map(AdminRole::getRoleId).collect(Collectors.toList());

        List<Role> roleList = this.lambdaQuery().select(Role::getCode).in(Role::getId, roleIdList).list();
        return roleList.stream().map(Role::getCode).collect(Collectors.toSet());
    }
}
