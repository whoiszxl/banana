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
@Schema(description = "组件查询条件")
public class SoftwareConfigQuery {

    @Schema(description = "组件ID")
    @Query
    private Integer softwareId;

}
