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
@Schema(description = "基础配置查询条件")
public class BaseConfigQuery {

    @Schema(description = "基础配置key或value模糊查询")
    @Query(blurry = "config_key,config_value")
    private String configKey;


}
