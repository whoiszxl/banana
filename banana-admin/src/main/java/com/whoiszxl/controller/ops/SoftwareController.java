package com.whoiszxl.controller.ops;


import com.whoiszxl.core.base.BaseController;
import com.whoiszxl.cqrs.command.SoftwareAddCommand;
import com.whoiszxl.cqrs.command.SoftwareUpdateCommand;
import com.whoiszxl.cqrs.query.SoftwareQuery;
import com.whoiszxl.cqrs.response.SoftwareResponse;
import com.whoiszxl.entity.Software;
import com.whoiszxl.service.ISoftwareService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 基础组件表 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Tag(name = "ops组件 API")
@RestController
@RequestMapping("/ops/software")
public class SoftwareController extends BaseController<ISoftwareService, Software, SoftwareResponse, SoftwareQuery, SoftwareAddCommand, SoftwareUpdateCommand> {

}

