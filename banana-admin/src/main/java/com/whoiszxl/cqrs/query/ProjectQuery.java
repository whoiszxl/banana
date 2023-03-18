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
public class ProjectQuery {

    @Schema(description = "项目名称或描述")
    @Query(blurry = "name,desc")
    private String name;
}
