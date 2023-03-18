package com.whoiszxl.strategy;

import com.whoiszxl.entity.Deploy;
import com.whoiszxl.entity.Software;

import java.util.List;

/**
 * 安装策略接口
 * @author whoiszxl
 */
public interface InstallStrategy {

    /**
     * 浏览服务器上的文件
     * @param absolutePath 文件绝对路径
     * @param serverId 服务器ID
     * @return 文件内容
     */
    String viewFile(String absolutePath, Integer serverId);

    /**
     * 对传入的服务器安装JDK
     * @param deploy 部署信息
     * @param software 组件信息
     * @return 是否安装成功
     */
    boolean installJdk(Deploy deploy, Software software);

    /**
     * 安装zookeeper
     * @param deploy 部署信息
     * @param software 组件信息
     * @return 是否安装成功
     */
    boolean installZookeeper(Deploy deploy, Software software);

    /**
     * 安装kafka
     * @param deploy 部署信息
     * @param software 组件信息
     * @return 是否安装成功
     */
    boolean installKafka(Deploy deploy, Software software);

    /**
     * 安装hadoop
     * @param deploy 部署信息
     * @param software 组件信息
     * @return 是否安装成功
     */
    boolean installHadoop(Deploy deploy, Software software);

    /**
     * flume安装
     * @param deploy 部署信息
     * @param software 组件信息
     * @return 是否安装成功
     */
    boolean installFlume(Deploy deploy, Software software);

    /**
     * Spark安装
     * @param deploy 部署信息
     * @param software 组件信息
     * @return 是否安装成功
     */
    boolean installSpark(Deploy deploy, Software software);

    /**
     * ES安装
     * @param deploy 部署信息
     * @param software 组件信息
     * @return 是否安装成功
     */
    boolean installEs(Deploy deploy, Software software);

    /**
     * kibana安装
     * @param deploy 部署信息
     * @param software 组件信息
     * @return 是否安装成功
     */
    boolean installKibana(Deploy deploy, Software software);
}