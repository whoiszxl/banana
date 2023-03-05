package com.whoiszxl.cqrs.query;

import com.whoiszxl.core.annotation.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author whoiszxl
 */
@Data
@ParameterObject
@Schema(description = "管理员查询条件")
public class RoleQuery {

    @Schema(description = "用户名")
    @Query(blurry = "username,real_name,mobile,email")
    private String username;

    @Schema(description = "状态")
    @Query
    private Integer status;

    @Schema(description = "start创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = Query.Type.GREATER_THAN_OR_EQUAL, property = "created_at")
    private Date startCreatedAt;

    @Schema(description = "end创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(type = Query.Type.LESS_THAN_OR_EQUAL, property = "created_at")
    private Date endCreatedAt;

}
