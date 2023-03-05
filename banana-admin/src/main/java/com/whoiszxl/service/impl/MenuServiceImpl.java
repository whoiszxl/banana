package com.whoiszxl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whoiszxl.entity.Menu;
import com.whoiszxl.mapper.MenuMapper;
import com.whoiszxl.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 系统菜单 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-02
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;


    @Override
    public Set<String> listPermissionsByAdminId(Integer adminId) {
        return menuMapper.listPermissionsByAdminId(adminId);
    }
}
