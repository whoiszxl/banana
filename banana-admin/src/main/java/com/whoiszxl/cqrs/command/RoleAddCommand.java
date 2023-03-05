package com.whoiszxl.cqrs.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-02
 */
@Data
@Schema(description = "角色添加命令")
public class RoleAddCommand implements Serializable {

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色代码")
    private String code;

    @Schema(description = "角色描述")
    private String description;
}
