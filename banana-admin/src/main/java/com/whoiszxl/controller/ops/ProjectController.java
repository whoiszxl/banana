package com.whoiszxl.controller.ops;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.whoiszxl.core.base.BaseController;
import com.whoiszxl.core.entity.ResponseResult;
import com.whoiszxl.cqrs.command.MenuAddCommand;
import com.whoiszxl.cqrs.command.ProjectAddCommand;
import com.whoiszxl.cqrs.command.ProjectUpdateCommand;
import com.whoiszxl.cqrs.query.ProjectQuery;
import com.whoiszxl.cqrs.response.ProjectResponse;
import com.whoiszxl.entity.Project;
import com.whoiszxl.service.IProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 项目表 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Tag(name = "ops项目 API")
@RestController
@RequestMapping("/ops/project")
public class ProjectController extends BaseController<IProjectService, Project, ProjectResponse, ProjectQuery, ProjectAddCommand, ProjectUpdateCommand> {

    @PostMapping("/generate/{projectId}")
    @SaCheckPermission(value = {"ops:project:generate"})
    @Operation(summary = "生成对应项目下需要的服务器", description = "生成对应项目下需要的服务器")
    public ResponseResult<String> generate(@PathVariable Integer projectId) {
        baseService.generate(projectId);
        return ResponseResult.buildSuccess();
    }

}

