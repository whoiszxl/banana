package com.whoiszxl.service;

import com.whoiszxl.entity.Deploy;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务部署表 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-13
 */
public interface IDeployService extends IService<Deploy> {

    /**
     * 一键部署
     * @param deployId 部署ID
     * @return 是否部署成功
     */
    boolean deploy(Integer deployId);

    /**
     * 初始化集群
     * @param deployId 部署ID
     * @return 是否初始化成功
     */
    boolean init(Integer deployId);

    /**
     * 启动集群
     * @param deployId 部署ID
     * @return 是否启动成功
     */
    String start(Integer deployId);

    /**
     * 集群状态
     * @param deployId 部署ID
     * @return 集群状态日志
     */
    String status(Integer deployId);

    /**
     * 同步组件到集群所有机器上
     * @param deployId 部署ID
     * @return 是否同步成功
     */
    boolean syncSoftware(Integer deployId);
}
