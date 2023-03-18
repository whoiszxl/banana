package com.whoiszxl.cqrs.dto.template;

import lombok.Data;


/**
 * @author zk模板参数实体
 */
@Data
public class ZookeeperTemplateParamsEntity {

    private String tickTime;

    private String initLimit;

    private String syncLimit;

    private String dataDir;

    private String dataLogDir;

    private String clientPort;

    private String maxClientCnxns;
}