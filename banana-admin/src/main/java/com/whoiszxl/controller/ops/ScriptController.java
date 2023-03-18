package com.whoiszxl.controller.ops;


import com.whoiszxl.core.base.BaseController;
import com.whoiszxl.cqrs.command.ScriptAddCommand;
import com.whoiszxl.cqrs.command.ScriptUpdateCommand;
import com.whoiszxl.cqrs.query.ScriptQuery;
import com.whoiszxl.cqrs.response.ScriptResponse;
import com.whoiszxl.entity.Script;
import com.whoiszxl.service.IScriptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * SH脚本表 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Tag(name = "ops脚本 API")
@RestController
@RequestMapping("/ops/script")
public class ScriptController extends BaseController<IScriptService, Script, ScriptResponse, ScriptQuery, ScriptAddCommand, ScriptUpdateCommand> {

}

