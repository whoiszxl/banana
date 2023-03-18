package com.whoiszxl.controller.ops;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.whoiszxl.core.base.BaseController;
import com.whoiszxl.core.entity.ResponseResult;
import com.whoiszxl.cqrs.command.ConnectTestCommand;
import com.whoiszxl.cqrs.command.ServerAddCommand;
import com.whoiszxl.cqrs.command.ServerUpdateCommand;
import com.whoiszxl.cqrs.query.ServerQuery;
import com.whoiszxl.cqrs.response.ServerResponse;
import com.whoiszxl.entity.Server;
import com.whoiszxl.service.IServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 服务器表 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Tag(name = "ops服务实例 API")
@RestController
@RequestMapping("/ops/server")
@RequiredArgsConstructor
public class ServerController extends BaseController<IServerService, Server, ServerResponse, ServerQuery, ServerAddCommand, ServerUpdateCommand> {

    private final IServerService serverService;


    @PostMapping("/connect/test")
    @SaCheckPermission(value = {"ops:server:connect-test"})
    @Operation(summary = "ssh连接测试", description = "测试服务器是否能连接")
    public ResponseResult<String> connectTest(@RequestBody ConnectTestCommand command) {
        boolean flag = serverService.connectTest(command);
        return ResponseResult.buildByFlag(flag);
    }
}

