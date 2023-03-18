package com.whoiszxl.cqrs.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 项目表
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Data
@Schema(description = "项目表")
public class ProjectResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "项目名称")
    private String name;

    @Schema(description = "项目描述")
    private String description;

    @Schema(description = "项目文档")
    private String markdown;

    @Schema(description = "服务器类型: 1-自建 2-阿里云 3-腾讯云")
    private Integer platformType;

    @Schema(description = "生成服务器平台的参数(json格式)")
    private String platformParams;

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
