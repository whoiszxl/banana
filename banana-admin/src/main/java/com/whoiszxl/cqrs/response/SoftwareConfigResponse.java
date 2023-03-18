package com.whoiszxl.cqrs.response;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 基础组件配置表
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Data
@Schema(description = "基础组件配置表")
public class SoftwareConfigResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "主键")
    private Integer softwareId;

    @Schema(description = "组件名")
    private String softwareName;

    @Schema(description = "组件文件名")
    private String configName;

    @Schema(description = "组件文件路径")
    private String configPath;

    @Schema(description = "组件文件模板")
    private String configTemplate;

    @Schema(description = "组件文件模板参数")
    private String configTemplateParams;

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
