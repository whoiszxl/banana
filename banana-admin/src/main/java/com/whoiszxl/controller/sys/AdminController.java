package com.whoiszxl.controller.sys;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.whoiszxl.core.base.BaseController;
import com.whoiszxl.core.entity.ResponseResult;
import com.whoiszxl.cqrs.command.AdminAddCommand;
import com.whoiszxl.cqrs.command.AdminRoleUpdateCommand;
import com.whoiszxl.cqrs.command.AdminUpdateCommand;
import com.whoiszxl.cqrs.query.AdminQuery;
import com.whoiszxl.cqrs.response.AdminDetailResponse;
import com.whoiszxl.entity.Admin;
import com.whoiszxl.service.IAdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-02
 */
@Tag(name = "admin管理员 API")
@RestController
@RequestMapping("/system/admin")
public class AdminController extends BaseController<IAdminService, Admin, AdminDetailResponse, AdminQuery, AdminAddCommand, AdminUpdateCommand> {


    @SaCheckPermission(value = {"system:admin:add"})
    public ResponseResult<String> addAdmin(@Validated @RequestBody AdminAddCommand command) {
        Boolean flag = baseService.addAdmin(command);
        return ResponseResult.buildByFlag(flag);
    }


    @SaCheckPermission(value = {"system:admin:update-role"})
    public ResponseResult<String> updateAdminRole(@Validated @RequestBody AdminRoleUpdateCommand command) {
        Boolean flag = baseService.updateAdminRole(command);
        return ResponseResult.buildByFlag(flag);
    }

}

