package com.whoiszxl.strategy;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.whoiszxl.cqrs.dto.InstanceGenerateDTO;

import java.util.List;

/**
 * 服务器生成策略
 * @author whoiszxl
 */
public interface ServerGenerateStrategy {


    /**
     * 生成服务实例
     * @param platformParams 平台参数
     * @return
     */
    InstanceGenerateDTO generateInstance(String platformParams);

    /**
     * 查看服务实例的状态
     * @param instanceIds 服务实例id数组
     * @return 服务实例状态
     */
    int describeInstancesStatus(String platformParams, String[] instanceIds);

    /**
     * 获取实例详情
     * @param instanceIdList 实例id列表
     * @return 实例详情列表
     */
    List<DescribeInstancesResponse.Instance> describeInstances(List<String> instanceIdList);
}
