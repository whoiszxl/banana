package com.whoiszxl.strategy.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.ecs.model.v20140526.RunInstancesRequest;
import com.aliyuncs.ecs.model.v20140526.RunInstancesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.whoiszxl.constants.ConfigConstants;
import com.whoiszxl.cqrs.dto.AliyunPlatformParams;
import com.whoiszxl.cqrs.dto.InstanceGenerateDTO;
import com.whoiszxl.service.IBaseConfigService;
import com.whoiszxl.strategy.ServerGenerateStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 阿里云服务器生成策略
 * @author whoiszxl
 */
@Slf4j
@RequiredArgsConstructor
@DependsOn("liquibase")
@Service("aliyunServerGenerateStrategy")
public class AliyunServerGenerateStrategy implements ServerGenerateStrategy {

    private static final int INSTANCE_STATUS_CHECK_INTERVAL_MILLISECOND = 3000;
    private static final String INSTANCE_STATUS_RUNNING = "Running";

    String accessKeyId;
    String accessSecret;
    String regionId;

    @PostConstruct
    public void init() {
        accessKeyId = baseConfigService.getByKey(ConfigConstants.ALIYUN_ACCESS_KEY_ID);
        accessSecret = baseConfigService.getByKey(ConfigConstants.ALIYUN_ACCESS_SECRET);
    }

    private final IBaseConfigService baseConfigService;

    @Override
    public InstanceGenerateDTO generateInstance(String platformParams) {
        AliyunPlatformParams params = JSONUtil.toBean(platformParams, AliyunPlatformParams.class);
        this.regionId = params.getRegionId();
        RunInstancesResponse response = callOpenApi(composeRunInstancesRequest(params));
        Assert.isFalse(response == null, "阿里云实例生成失败");
        return new InstanceGenerateDTO(response.getInstanceIdSets(), response.getRequestId());
    }

    @Override
    public int describeInstancesStatus(String platformParams, String[] instanceIds) {
        return 0;
    }

    @Override
    public List<DescribeInstancesResponse.Instance> describeInstances(List<String> instanceIdList) {
        for(;;) {
            sleepSomeTime(INSTANCE_STATUS_CHECK_INTERVAL_MILLISECOND);
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setSysRegionId(regionId);
            describeInstancesRequest.setInstanceIds(JSONUtil.toJsonStr(instanceIdList));
            DescribeInstancesResponse describeInstancesResponse = callOpenApi(describeInstancesRequest);
            Long timeStamp = System.currentTimeMillis();
            if(describeInstancesResponse == null) {
                continue;
            } else {
                int successCount = 0;
                for(DescribeInstancesResponse.Instance instance : describeInstancesResponse.getInstances()) {
                    if(INSTANCE_STATUS_RUNNING.equals(instance.getStatus())) {
                        successCount++;
                    }
                }

                if(successCount == instanceIdList.size()) {
                    return describeInstancesResponse.getInstances();
                }
            }
        }
    }

    private RunInstancesRequest composeRunInstancesRequest(AliyunPlatformParams params) {
        RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
        runInstancesRequest.setDryRun(false);
        runInstancesRequest.setSysRegionId(params.getRegionId());
        runInstancesRequest.setInstanceType(params.getInstanceType());
        runInstancesRequest.setInstanceChargeType(params.getInstanceChargeType());
        runInstancesRequest.setImageId(params.getImageId());
        runInstancesRequest.setSecurityGroupId(params.getSecurityGroupId());
        runInstancesRequest.setPeriod(params.getPeriod());
        runInstancesRequest.setPeriodUnit(params.getPeriodUnit());
        runInstancesRequest.setZoneId(params.getZoneId());
        runInstancesRequest.setInternetChargeType(params.getInternetChargeType());
        runInstancesRequest.setVSwitchId(params.getVSwitchId());
        runInstancesRequest.setInstanceName(params.getInstanceName());
        runInstancesRequest.setAmount(params.getInstanceQuantity());
        runInstancesRequest.setInternetMaxBandwidthOut(params.getInternetMaxBandwidthOut());
        runInstancesRequest.setHostName(params.getHostName());
        runInstancesRequest.setUniqueSuffix(params.isUniqueSuffix());
        runInstancesRequest.setIoOptimized(params.getIoOptimized());
        runInstancesRequest.setSpotStrategy(params.getSpotStrategy());
        runInstancesRequest.setSpotPriceLimit(params.getSpotPriceLimit());
        runInstancesRequest.setSecurityEnhancementStrategy(params.getSecurityEnhancementStrategy());
        runInstancesRequest.setSystemDiskSize(params.getSystemDiskSize());
        runInstancesRequest.setSystemDiskCategory(params.getSystemDiskCategory());
        runInstancesRequest.setPassword(params.getPassword());

        DateTime dateTime = DateUtil.offsetHour(DateUtil.date(), params.getTime() - 8);
        dateTime.offset(DateField.SECOND, 60);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        runInstancesRequest.setAutoReleaseTime(sdf.format(dateTime.toJdkDate()));

        return runInstancesRequest;
    }



    /**
     * 调用OpenAPI的方法，这里进行了错误处理
     *
     * @param request AcsRequest, Open API的请求
     * @param <T> AcsResponse 请求所对应返回值
     * @return 返回OpenAPI的调用结果，如果调用失败，则会返回null
     */
    private <T extends AcsResponse> T callOpenApi(AcsRequest<T> request) {
        IAcsClient client = initClient();
        try {
            T response = client.getAcsResponse(request, false, 0);
            log.info("OpenAPI调用成功:{}", request.getSysActionName());
            return response;
        } catch (ServerException e) {
            log.info("OpenAPI调用失败:{}:{}", e.getErrCode(), e.getMessage());
        } catch (ClientException e) {
            log.info("OpenAPI客户端调用失败:{}:{}", e.getErrCode(), e.getMessage());
        }
        return null;
    }

    private IAcsClient initClient() {
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessSecret);
        return new DefaultAcsClient(profile);
    }

    private static void sleepSomeTime(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
