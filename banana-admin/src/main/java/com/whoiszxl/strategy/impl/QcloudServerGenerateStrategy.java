package com.whoiszxl.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.*;
import com.whoiszxl.core.exception.ExceptionCatcher;
import com.whoiszxl.cqrs.dto.InstanceGenerateDTO;
import com.whoiszxl.cqrs.dto.QcloudPlatformParams;
import com.whoiszxl.strategy.ServerGenerateStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 腾讯云服务器生成策略
 * @author whoiszxl
 */
@Slf4j
@Service("qcloudServerGenerateStrategy")
public class QcloudServerGenerateStrategy implements ServerGenerateStrategy {

    @Override
    public InstanceGenerateDTO generateInstance(String platformParams) {
        QcloudPlatformParams params = JSONUtil.toBean(platformParams, QcloudPlatformParams.class);

        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential("AKIDa3b8uc6MRHtFPN6Zt12iMp6EDzaHo8E7", "iJl13pMguvjIgroW3NTK1Xrz4IK9mAMR");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(params.getEndpoint());
            // 实例化一个client选项，可选的，没有特殊需求可以跳过


            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);


            // 实例化要请求产品的client对象,clientProfile是可选的
            CvmClient client = new CvmClient(cred, params.getZone1(), clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            RunInstancesRequest req = new RunInstancesRequest();
            req.setInstanceChargeType(params.getInstanceChargeType());
            req.setDisableApiTermination(false);
            Placement placement1 = new Placement();
            placement1.setZone(params.getZone2());
            placement1.setProjectId(0L);
            req.setPlacement(placement1);

            InstanceMarketOptionsRequest instanceMarketOptionsRequest1 = new InstanceMarketOptionsRequest();
            SpotMarketOptions spotMarketOptions1 = new SpotMarketOptions();
            spotMarketOptions1.setMaxPrice(params.getMaxPrice());
            instanceMarketOptionsRequest1.setSpotOptions(spotMarketOptions1);

            req.setInstanceMarketOptions(instanceMarketOptionsRequest1);

            VirtualPrivateCloud virtualPrivateCloud1 = new VirtualPrivateCloud();
            virtualPrivateCloud1.setAsVpcGateway(params.isAsVpcGateway());
            virtualPrivateCloud1.setVpcId(params.getVpcId());
            virtualPrivateCloud1.setSubnetId(params.getSubnetId());
            virtualPrivateCloud1.setIpv6AddressCount((long) params.getIpv6AddressCount());
            req.setVirtualPrivateCloud(virtualPrivateCloud1);

            req.setInstanceType(params.getInstanceType());
            req.setImageId(params.getImageId());
            SystemDisk systemDisk1 = new SystemDisk();
            systemDisk1.setDiskSize(params.getDiskSize());
            systemDisk1.setDiskType(params.getDiskType());
            req.setSystemDisk(systemDisk1);

            InternetAccessible internetAccessible1 = new InternetAccessible();
            internetAccessible1.setInternetMaxBandwidthOut(params.getInternetMaxBandwidthOut());
            internetAccessible1.setPublicIpAssigned(params.isPublicIpAssigned());
            internetAccessible1.setInternetChargeType(params.getInternetChargeType());
            req.setInternetAccessible(internetAccessible1);

            req.setInstanceName(params.getInstanceName());
            LoginSettings loginSettings1 = new LoginSettings();
            loginSettings1.setPassword(params.getPassword());
            req.setLoginSettings(loginSettings1);


            req.setSecurityGroupIds(params.getSecurityGroupIds().toArray(new String[0]));

            req.setInstanceCount(params.getInstanceCount());
            EnhancedService enhancedService1 = new EnhancedService();
            RunSecurityServiceEnabled runSecurityServiceEnabled1 = new RunSecurityServiceEnabled();
            runSecurityServiceEnabled1.setEnabled(params.isRunSecurityServiceEnabled());
            enhancedService1.setSecurityService(runSecurityServiceEnabled1);

            RunMonitorServiceEnabled runMonitorServiceEnabled1 = new RunMonitorServiceEnabled();
            runMonitorServiceEnabled1.setEnabled(params.isRunMonitorServiceEnabled());
            enhancedService1.setMonitorService(runMonitorServiceEnabled1);

            RunAutomationServiceEnabled runAutomationServiceEnabled1 = new RunAutomationServiceEnabled();
            runAutomationServiceEnabled1.setEnabled(params.isRunAutomationServiceEnabled());
            enhancedService1.setAutomationService(runAutomationServiceEnabled1);

            req.setEnhancedService(enhancedService1);

            // 返回的resp是一个RunInstancesResponse的实例，与请求对象对应
            RunInstancesResponse resp = client.RunInstances(req);
            // 输出json格式的字符串回包
            System.out.println(RunInstancesResponse.toJsonString(resp));

            String[] instanceIdSet = resp.getInstanceIdSet();
            String requestId = resp.getRequestId();

            return new InstanceGenerateDTO(Arrays.asList(instanceIdSet), requestId);
        } catch (TencentCloudSDKException e) {
            log.error("QcloudServerGenerateStrategy|腾讯云服务器实例生成失败|", e);
            ExceptionCatcher.catchServiceEx("腾讯云服务器实例生成失败");
        }
        return null;
    }

    @Override
    public int describeInstancesStatus(String platformParams, String[] instanceIds) {
        try{
            QcloudPlatformParams params = JSONUtil.toBean(platformParams, QcloudPlatformParams.class);
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential("AKIDa3b8uc6MRHtFPN6Zt12iMp6EDzaHo8E7", "iJl13pMguvjIgroW3NTK1Xrz4IK9mAMR");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(params.getEndpoint());
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            CvmClient client = new CvmClient(cred,  params.getZone1(), clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DescribeInstancesStatusRequest req = new DescribeInstancesStatusRequest();
            req.setInstanceIds(instanceIds);

            // 返回的resp是一个DescribeInstancesStatusResponse的实例，与请求对象对应
            DescribeInstancesStatusResponse resp = client.DescribeInstancesStatus(req);
            InstanceStatus[] instanceStatusSet = resp.getInstanceStatusSet();
            // 输出json格式的字符串回包
            System.out.println(DescribeInstancesStatusResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return 1;
    }

    @Override
    public List<com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse.Instance> describeInstances(List<String> instanceIdList) {
        return null;
    }
}
