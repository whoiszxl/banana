package com.whoiszxl.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whoiszxl.entity.AdminRole;
import com.whoiszxl.mapper.AdminRoleMapper;
import com.whoiszxl.service.IAdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 管理员&角色关联表 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-02
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements IAdminRoleService {

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Override
    public void save(List<Integer> roleIds, Integer id) {
        if(CollUtil.isEmpty(roleIds)) {
            return;
        }

        this.lambdaUpdate().eq(AdminRole::getAdminId, id).remove();

        List<AdminRole> adminRoleList
                = roleIds.stream().map(roleId -> new AdminRole(roleId, id)).collect(Collectors.toList());
        adminRoleMapper.insertBatch(adminRoleList);
    }

    @Override
    public void resetRole(List<Integer> roleIds, Integer adminId) {
        boolean removeFlag = this.lambdaUpdate().eq(AdminRole::getAdminId, adminId).remove();
        Assert.isTrue(removeFlag, "清除原角色信息失败");

        List<AdminRole> adminRoleEntityList
                = roleIds.stream().map(roleId -> new AdminRole(roleId, adminId)).collect(Collectors.toList());
        adminRoleMapper.insertBatch(adminRoleEntityList);
    }


}
