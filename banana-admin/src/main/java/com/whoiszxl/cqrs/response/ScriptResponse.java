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
 * SH脚本表
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Data
@Schema(description = "SH脚本表")
public class ScriptResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "脚本标题")
    private String scriptTitle;

    @Schema(description = "脚本名称")
    private String scriptName;

    @Schema(description = "脚本路径")
    private String scriptPath;

    @Schema(description = "脚本内容")
    private String scriptContent;

    @Schema(description = "脚本描述")
    private String description;

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
