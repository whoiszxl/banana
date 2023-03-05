package com.whoiszxl.logger.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * log配置
 * @author whoiszxl
 */
@Data
@Component
@ConfigurationProperties(prefix = "banana.logger")
public class LogProperties {

    /**
     * 是否启用系统日志
     */
    private Boolean enabled;

    /**
     * 是否记录内网IP
     */
    private Boolean includeInnerIp;

    /**
     * 不记录系统日志的请求方式列表
     */
    private List<String> excludeMethods = new ArrayList<>();

    /**
     * 脱敏字段
     */
    private List<String> desensitize = new ArrayList<>();
}
