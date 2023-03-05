package com.whoiszxl.cqrs.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author whoiszxl
 */
@Data
@Schema(description = "管理员角色修改命令")
public class AdminRoleUpdateCommand {

    @NotEmpty(message = "管理员ID不能为空")
    @Schema(description = "管理员ID")
    private Integer adminId;

    @NotEmpty(message = "角色ID集合不能为空")
    @Schema(description = "需要修改的角色ID集合")
    private List<Integer> roleIds;
}
