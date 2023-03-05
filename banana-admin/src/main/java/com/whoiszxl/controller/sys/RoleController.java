package com.whoiszxl.controller.sys;


import com.whoiszxl.core.base.BaseController;
import com.whoiszxl.cqrs.command.RoleAddCommand;
import com.whoiszxl.cqrs.command.RoleUpdateCommand;
import com.whoiszxl.cqrs.query.RoleQuery;
import com.whoiszxl.cqrs.response.RoleDetailResponse;
import com.whoiszxl.entity.Role;
import com.whoiszxl.service.IRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-02
 */
@Tag(name = "role角色 API")
@RestController
@RequestMapping("/system/role")
public class RoleController extends BaseController<IRoleService, Role, RoleDetailResponse, RoleQuery, RoleAddCommand, RoleUpdateCommand> {

}

