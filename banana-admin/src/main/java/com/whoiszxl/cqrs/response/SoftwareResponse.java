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
 * 基础组件表
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Data
@Schema(description = "基础组件表")
public class SoftwareResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "组件名")
    private String softwareName;

    @Schema(description = "组件文件名")
    private String softwareFilename;

    @Schema(description = "组件文件路径")
    private String softwarePath;

    @Schema(description = "组件安装路径")
    private String installPath;

    @Schema(description = "环境变量路径")
    private String envPath;

    @Schema(description = "环境变量内容")
    private String envContent;

    @Schema(description = "安装脚本路径")
    private String installScriptPath;

    @Schema(description = "启动脚本路径")
    private String startScriptPath;

    @Schema(description = "状态脚本路径")
    private String statusScriptPath;

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
