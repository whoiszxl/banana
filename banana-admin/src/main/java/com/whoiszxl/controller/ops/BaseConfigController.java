package com.whoiszxl.controller.ops;


import com.whoiszxl.core.base.BaseController;
import com.whoiszxl.cqrs.command.BaseConfigAddCommand;
import com.whoiszxl.cqrs.command.BaseConfigUpdateCommand;
import com.whoiszxl.cqrs.query.BaseConfigQuery;
import com.whoiszxl.cqrs.response.BaseConfigResponse;
import com.whoiszxl.entity.BaseConfig;
import com.whoiszxl.service.IBaseConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 基础配置表 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Tag(name = "ops基础配置 API")
@RestController
@RequestMapping("/ops/base-config")
public class BaseConfigController extends BaseController<IBaseConfigService, BaseConfig, BaseConfigResponse, BaseConfigQuery, BaseConfigAddCommand, BaseConfigUpdateCommand> {

}

