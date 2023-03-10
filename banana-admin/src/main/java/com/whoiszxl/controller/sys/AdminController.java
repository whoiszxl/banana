package com.whoiszxl.controller.sys;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import com.whoiszxl.core.base.BaseController;
import com.whoiszxl.core.entity.ResponseResult;
import com.whoiszxl.core.token.enums.PermissionNameEnum;
import com.whoiszxl.cqrs.command.AdminAddCommand;
import com.whoiszxl.cqrs.command.AdminRoleUpdateCommand;
import com.whoiszxl.cqrs.command.AdminUpdateCommand;
import com.whoiszxl.cqrs.query.AdminQuery;
import com.whoiszxl.cqrs.response.AdminDetailResponse;
import com.whoiszxl.cqrs.response.AdminResponse;
import com.whoiszxl.entity.Admin;
import com.whoiszxl.service.IAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
public class AdminController extends BaseController<IAdminService, Admin, AdminResponse, AdminQuery, AdminAddCommand, AdminUpdateCommand> {


    @PostMapping("/add")
    @SaCheckPermission(value = {"system:admin:add"})
    @Operation(summary = "管理后台新增管理员", description = "管理后台新增管理员")
    public ResponseResult<String> addAdmin(@Validated @RequestBody AdminAddCommand command) {
        Boolean flag = baseService.addAdmin(command);
        return ResponseResult.buildByFlag(flag);
    }

    @PatchMapping("/update/role")
    @SaCheckPermission(value = {"system:admin:update-role"})
    @Operation(summary = "更新管理员的角色", description = "更新管理员的角色")
    public ResponseResult<String> updateAdminRole(@Validated @RequestBody AdminRoleUpdateCommand command) {
        Boolean flag = baseService.updateAdminRole(command);
        return ResponseResult.buildByFlag(flag);
    }

    @Operation(summary = "切换管理员的状态", description = "切换管理员状态")
    @PatchMapping("/switch/status/{adminId}")
    @SaCheckPermission(value = {"system:admin:switch-status"})
    public ResponseResult<String> switchStatus(@PathVariable Integer adminId) {
        Boolean flag = baseService.switchStatus(adminId);
        return ResponseResult.buildByFlag(flag);
    }

    @Operation(summary = "管理后台更新管理员", description = "管理后台更新管理员")
    @ResponseBody
    @SaCheckPermission(value = {"system:admin:update"})
    @PutMapping("/update")
    protected ResponseResult<String> updateAdmin(@RequestBody AdminUpdateCommand adminUpdateCommand) {
        boolean flag = baseService.updateAdmin(adminUpdateCommand);
        return ResponseResult.buildByFlag(flag);
    }

    @PatchMapping("/reset/password/{adminId}")
    @SaCheckPermission(value = {"system:admin:reset-password"})
    @Operation(summary = "重置管理员密码", description = "重置管理员密码")
    public ResponseResult<String> resetPassword(@PathVariable Integer adminId) {
        Boolean flag = baseService.resetPassword(adminId);
        return ResponseResult.buildByFlag(flag);
    }

    @GetMapping("/detail/{adminId}")
    @SaCheckPermission(value = {"system:admin:list"})
    @Operation(summary = "获取管理员的详情信息", description = "获取管理员的详情信息,并带上角色相关信息")
    public ResponseResult<AdminDetailResponse> adminDetail(@PathVariable Integer adminId) {
        AdminDetailResponse detailResponse = baseService.adminDetail(adminId);
        return ResponseResult.buildSuccess(detailResponse);
    }

    @Operation(summary = "批量删除管理员", description = "传入管理员ID集合，批量删除管理员")
    @SaCheckPermission(value = {"system:admin:delete"})
    @ResponseBody
    @DeleteMapping("/delete/{ids}")
    protected ResponseResult adminDelete(@PathVariable List<Integer> ids) {
        boolean flag = baseService.adminDelete(ids);
        return ResponseResult.buildByFlag(flag);
    }
}

