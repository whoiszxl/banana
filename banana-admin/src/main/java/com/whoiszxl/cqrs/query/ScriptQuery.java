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
@Schema(description = "脚本查询条件")
public class ScriptQuery {

    @Schema(description = "脚本名或描述")
    @Query(blurry = "script_name,description")
    private String scriptName;


}
