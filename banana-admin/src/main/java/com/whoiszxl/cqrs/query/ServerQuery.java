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
@Schema(description = "项目查询条件")
public class ServerQuery {

    @Schema(description = "服务器名称或外网ip")
    @Query(blurry = "server_name,server_outer_ip")
    private String serverName;

    @Query
    @Schema(description = "项目ID")
    private Integer projectId;

    @Query
    @Schema(description = "服务器类型: 1-自建 2-阿里云 3-腾讯云")
    private Integer platformType;
}
