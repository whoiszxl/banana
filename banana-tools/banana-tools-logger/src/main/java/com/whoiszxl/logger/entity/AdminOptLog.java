package com.whoiszxl.logger.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统日志
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-02
 */
@Getter
@Setter
@TableName("sys_admin_opt_log")
@Schema(description = "系统日志")
public class AdminOptLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "操作人")
    private String adminName;

    @Schema(description = "操作IP")
    private String requestIp;

    @Schema(description = "请求地址")
    private String requestUri;

    @Schema(description = "请求类型#HttpMethod{GET:GET请求;POST:POST请求;PUT:PUT请求;DELETE:DELETE请求;PATCH:PATCH请求;TRACE:TRACE请求;HEAD:HEAD请求;OPTIONS:OPTIONS请求;}")
    private String requestHttpMethod;

    @Schema(description = "请求头")
    private String requestHeaders;

    @Schema(description = "请求体")
    private String requestBody;

    @Schema(description = "状态码")
    private Integer statusCode;

    @Schema(description = "操作描述")
    private String description;

    @Schema(description = "类路径")
    private String classPath;

    @Schema(description = "请求方法")
    private String actionMethod;

    @Schema(description = "响应头")
    private String responseHeader;

    @Schema(description = "响应体")
    private String responseResult;

    @Schema(description = "异常详情信息")
    private String exDesc;

    @Schema(description = "异常描述")
    private String exDetail;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "消耗时间(ms)")
    private Long consumingTime;

    @Schema(description = "浏览器")
    private String ua;

    @Schema(description = "乐观锁")
    @Version
    private Long version;

    @Schema(description = "业务状态 1:成功 2:失败")
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
