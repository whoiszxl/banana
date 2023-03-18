package com.whoiszxl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务器表
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Getter
@Setter
@TableName("ops_server")
@Schema(description = "服务器表")
public class Server implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "管理员ID")
    private Integer adminId;

    @Schema(description = "项目ID")
    private Integer projectId;

    @Schema(description = "服务器名称")
    private String serverName;

    @Schema(description = "服务器外网IP")
    private String serverOuterIp;

    @Schema(description = "服务器内网IP")
    private String serverInnerIp;

    @Schema(description = "服务器端口")
    private String serverPort;

    @Schema(description = "服务器用户名")
    private String serverUsername;

    @Schema(description = "服务器密码")
    private String serverPassword;

    @Schema(description = "服务器类型: 1-自建 2-阿里云 3-腾讯云")
    private Integer platformType;

    @Schema(description = "乐观锁")
    @Version
    private Long version;

    @Schema(description = "业务状态")
    private Integer status;

    @Schema(description = "逻辑删除 1: 已删除， 0: 未删除")
    @TableLogic
    private Integer isDeleted;

    @Schema(description = "创建者")
    private String createdBy;

    @Schema(description = "更新者")
    private String updatedBy;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;


}
