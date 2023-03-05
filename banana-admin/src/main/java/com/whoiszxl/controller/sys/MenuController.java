package com.whoiszxl.controller.sys;


import com.whoiszxl.core.base.BaseController;
import com.whoiszxl.cqrs.command.MenuAddCommand;
import com.whoiszxl.cqrs.command.MenuUpdateCommand;
import com.whoiszxl.cqrs.query.MenuQuery;
import com.whoiszxl.cqrs.response.MenuDetailResponse;
import com.whoiszxl.entity.Menu;
import com.whoiszxl.service.IMenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统菜单 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-02
 */
@Tag(name = "menu菜单 API")
@RestController
@RequestMapping("/system/menu")
public class MenuController extends BaseController<IMenuService, Menu, MenuDetailResponse, MenuQuery, MenuAddCommand, MenuUpdateCommand> {

}

