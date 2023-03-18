package com.whoiszxl.controller.ops;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.whoiszxl.core.base.BaseController;
import com.whoiszxl.core.entity.ResponseResult;
import com.whoiszxl.cqrs.command.DeployAddCommand;
import com.whoiszxl.cqrs.command.DeployUpdateCommand;
import com.whoiszxl.cqrs.query.DeployQuery;
import com.whoiszxl.cqrs.response.DeployResponse;
import com.whoiszxl.entity.Deploy;
import com.whoiszxl.service.IDeployService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 服务部署表 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-13
 */
@Tag(name = "ops部署 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ops/deploy")
public class DeployController extends BaseController<IDeployService, Deploy, DeployResponse, DeployQuery, DeployAddCommand, DeployUpdateCommand> {

    private final IDeployService deployService;

    @PostMapping("/deploy/{deployId}")
    @SaCheckPermission(value = {"ops:deploy:deploy"})
    @Operation(summary = "应用一键部署", description = "应用一键部署")
    public ResponseResult<String> deploy(@PathVariable Integer deployId) {
        boolean flag = deployService.deploy(deployId);
        return ResponseResult.buildByFlag(flag);
    }


    @PostMapping("/init/{deployId}")
    @SaCheckPermission(value = {"ops:deploy:deploy"})
    @Operation(summary = "初始化集群", description = "初始化集群")
    public ResponseResult<String> init(@PathVariable Integer deployId) {
        boolean flag = deployService.init(deployId);
        return ResponseResult.buildByFlag(flag);
    }

    @PostMapping("/start/{deployId}")
    @SaCheckPermission(value = {"ops:deploy:deploy"})
    @Operation(summary = "集群启动", description = "集群启动")
    public ResponseResult<String> start(@PathVariable Integer deployId) {
        String result = deployService.start(deployId);
        return ResponseResult.buildSuccess(result);
    }



    @PostMapping("/status/{deployId}")
    @SaCheckPermission(value = {"ops:deploy:deploy"})
    @Operation(summary = "集群状态", description = "集群状态")
    public ResponseResult<String> status(@PathVariable Integer deployId) {
        String result = deployService.status(deployId);
        return ResponseResult.buildSuccess(result);
    }


    @PostMapping("/sync/software/{deployId}")
    @SaCheckPermission(value = {"ops:deploy:deploy"})
    @Operation(summary = "同步组件到集群所有机器上", description = "同步组件到集群所有机器上")
    public ResponseResult<String> syncSoftware(@PathVariable Integer deployId) {
        boolean flag = deployService.syncSoftware(deployId);
        return ResponseResult.buildByFlag(flag);
    }

}

