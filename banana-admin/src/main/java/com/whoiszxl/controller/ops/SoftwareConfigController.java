package com.whoiszxl.controller.ops;


import com.whoiszxl.core.base.BaseController;
import com.whoiszxl.cqrs.command.SoftwareConfigAddCommand;
import com.whoiszxl.cqrs.command.SoftwareConfigUpdateCommand;
import com.whoiszxl.cqrs.query.SoftwareConfigQuery;
import com.whoiszxl.cqrs.response.SoftwareConfigResponse;
import com.whoiszxl.entity.SoftwareConfig;
import com.whoiszxl.service.ISoftwareConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 基础组件配置表 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Tag(name = "ops组件配置 API")
@RestController
@RequestMapping("/ops/software-config")
public class SoftwareConfigController extends BaseController<ISoftwareConfigService, SoftwareConfig, SoftwareConfigResponse, SoftwareConfigQuery, SoftwareConfigAddCommand, SoftwareConfigUpdateCommand> {

}

