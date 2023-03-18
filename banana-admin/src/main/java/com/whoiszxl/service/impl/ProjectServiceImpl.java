package com.whoiszxl.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.whoiszxl.core.token.TokenHelper;
import com.whoiszxl.cqrs.dto.AliyunPlatformParams;
import com.whoiszxl.cqrs.dto.InstanceGenerateDTO;
import com.whoiszxl.entity.Project;
import com.whoiszxl.entity.Server;
import com.whoiszxl.enums.PlatformTypeEnum;
import com.whoiszxl.mapper.ProjectMapper;
import com.whoiszxl.service.IProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whoiszxl.service.IServerService;
import com.whoiszxl.strategy.ServerGenerateStrategy;
import com.whoiszxl.strategy.ServerGenerateStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    private final IServerService serverService;

    private final TokenHelper tokenHelper;

    private final ServerGenerateStrategyFactory serverGenerateStrategyFactory;

    @Override
    public void generate(Integer projectId) {
        //1. 获取项目对应的平台参数，进行对应生成
        Long memberId = tokenHelper.getMemberId();
        boolean exists = serverService.lambdaQuery().eq(Server::getAdminId, memberId).exists();
        Assert.isFalse(exists, "已存在，不允许重复生成");

        Project project = this.getById(projectId);
        Integer platformType = project.getPlatformType();
        String platformParams = project.getPlatformParams();
        AliyunPlatformParams aliyunPlatformParams = JSONUtil.toBean(platformParams, AliyunPlatformParams.class);

        ServerGenerateStrategy strategy = serverGenerateStrategyFactory.getStrategy(PlatformTypeEnum.find(platformType).getName());
        InstanceGenerateDTO instanceGenerateDTO = strategy.generateInstance(platformParams);

        //2. 将生成的服务器保存到server表
        List<DescribeInstancesResponse.Instance> instanceList =  strategy.describeInstances(instanceGenerateDTO.getInstanceIdList());

        List<Server> saveList = new ArrayList<>();
        for (DescribeInstancesResponse.Instance instance : instanceList) {
            Server server = new Server();
            server.setAdminId(memberId.intValue());
            server.setServerName(instance.getHostName());
            server.setServerUsername("root");
            server.setServerPassword(aliyunPlatformParams.getPassword());
            server.setServerPort("22");
            server.setPlatformType(platformType);
            server.setProjectId(projectId);

            if(CollectionUtils.isNotEmpty(instance.getPublicIpAddress())) {
                server.setServerOuterIp(instance.getPublicIpAddress().get(0));
            }
            if(CollectionUtils.isNotEmpty(instance.getInnerIpAddress())) {
                server.setServerInnerIp(instance.getInnerIpAddress().get(0));
            }else if(CollectionUtils.isNotEmpty(instance.getNetworkInterfaces())) {
                server.setServerInnerIp(instance.getNetworkInterfaces().get(0).getPrimaryIpAddress());
            }

            saveList.add(server);
        }

        serverService.saveBatch(saveList);

    }
}
