package com.whoiszxl.cqrs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 阿里云平台参数
 * @author whoiszxl
 */
@Data
@Schema(description = "阿里云平台参数")
public class AliyunPlatformParams {

    private String regionId;
    private String instanceType;
    private String instanceChargeType;
    private String imageId;
    private String securityGroupId;
    private Integer period;
    private String periodUnit;
    private String zoneId;
    private String internetChargeType;
    private String vSwitchId;
    private int internetMaxBandwidthOut;
    private boolean uniqueSuffix;
    private String ioOptimized;
    private String spotStrategy;
    private String securityEnhancementStrategy;
    private String systemDiskSize;
    private String systemDiskCategory;

    private Float spotPriceLimit;
    private String instanceName;
    private Integer instanceQuantity;
    private String hostName;
    private String password;
    private Integer time;

}
