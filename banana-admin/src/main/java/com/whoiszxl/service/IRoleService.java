package com.whoiszxl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whoiszxl.entity.Role;

import java.util.Set;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-02
 */
public interface IRoleService extends IService<Role> {

    /**
     * 获取此用户下的所有角色
     * @param adminId
     * @return
     */
    Set<String> listRoleByAdminId(Integer adminId);
}
