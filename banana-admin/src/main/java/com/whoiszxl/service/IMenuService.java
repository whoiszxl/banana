package com.whoiszxl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whoiszxl.entity.Menu;

import java.util.Set;

/**
 * <p>
 * 系统菜单 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-02
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 通过管理员id获取此管理员的所有权限
     * @param adminId 管理员ID
     * @return 权限列表
     */
    Set<String> listPermissionsByAdminId(Integer adminId);
}
