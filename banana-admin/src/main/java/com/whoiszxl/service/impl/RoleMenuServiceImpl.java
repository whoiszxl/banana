package com.whoiszxl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whoiszxl.entity.RoleMenu;
import com.whoiszxl.mapper.RoleMenuMapper;
import com.whoiszxl.service.IRoleMenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色与菜单关联表 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-02
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

}
