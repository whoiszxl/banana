package com.whoiszxl.cqrs.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务部署表
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-13
 */
@Data
@Schema(description = "服务部署表")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeployUpdateCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "管理员ID")
    private Integer adminId;

    @Schema(description = "部署名称")
    private String deployName;

    @Schema(description = "部署描述")
    private String description;

    @Schema(description = "部署组件ID")
    private Integer softwareId;

    @Schema(description = "部署服务实例ID集合")
    private String serverIds;

    @Schema(description = "部署日志")
    private String deployLogs;

    @Schema(description = "业务状态: 0-未初始化 1-已初始化")
    private Integer initStatus;
    
    @Schema(description = "业务状态: 1-部署成功 2-部署失败 3-正在部署中")
    private Integer status;

}
