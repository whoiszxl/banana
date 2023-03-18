package com.whoiszxl.cqrs.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 基础配置表
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Data
@Schema(description = "基础配置表")
public class BaseConfigResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "组件名")
    private String configKey;

    @Schema(description = "组件文件名")
    private String configValue;

    @Schema(description = "业务状态")
    private Integer status;

    @Schema(description = "创建者")
    private String createdBy;

    @Schema(description = "更新者")
    private String updatedBy;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;


}
