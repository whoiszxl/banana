package com.whoiszxl.cqrs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 腾讯云平台参数
 * @author whoiszxl
 */
@Data
@Schema(description = "腾讯云平台参数")
public class QcloudPlatformParams {

    private String endpoint;
    private String zone1;
    private String zone2;
    private String instanceChargeType;
    private boolean disableApiTermination;
    private String maxPrice;
    private boolean asVpcGateway;
    private String vpcId;
    private String subnetId;
    private int ipv6AddressCount;
    private String instanceType;
    private String imageId;
    private long diskSize;
    private String diskType;
    private long internetMaxBandwidthOut;
    private boolean publicIpAssigned;
    private String internetChargeType;
    private String instanceName;
    private String password;
    private List<String> securityGroupIds;
    private long instanceCount;
    private boolean runSecurityServiceEnabled;
    private boolean runMonitorServiceEnabled;
    private boolean runAutomationServiceEnabled;
}
