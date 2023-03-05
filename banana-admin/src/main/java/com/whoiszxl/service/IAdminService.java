package com.whoiszxl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whoiszxl.cqrs.command.AdminAddCommand;
import com.whoiszxl.cqrs.command.AdminRoleUpdateCommand;
import com.whoiszxl.entity.Admin;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-02
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 管理后台新增管理员
     * @param command 新增命令
     * @return 是否新增成功
     */
    Boolean addAdmin(AdminAddCommand command);

    /**
     * 修改管理员角色
     * @param command 更新角色命令
     * @return 是否更新成功
     */
    Boolean updateAdminRole(AdminRoleUpdateCommand command);
}
