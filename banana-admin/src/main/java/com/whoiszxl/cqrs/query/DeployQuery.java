package com.whoiszxl.cqrs.query;

import com.whoiszxl.core.annotation.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

/**
 * @author whoiszxl
 */
@Data
@ParameterObject
@Schema(description = "部署查询条件")
public class DeployQuery {

    @Schema(description = "管理员ID")
    @Query
    private Integer adminId;

    @Schema(description = "部署名称或描述")
    @Query(blurry = "deploy_name,description")
    private String deployName;

    @Schema(description = "部署组件ID")
    @Query
    private Integer softwareId;

    @Schema(description = "业务状态: 1-部署成功 2-部署失败 3-正在部署中")
    @Query
    private Integer status;

}
