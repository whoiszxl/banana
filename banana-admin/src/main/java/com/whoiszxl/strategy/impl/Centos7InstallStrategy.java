package com.whoiszxl.strategy.impl;

import cn.hutool.core.text.CharPool;
import cn.hutool.json.JSONUtil;
import com.whoiszxl.constants.SoftwareConfigNameConstants;
import com.whoiszxl.core.utils.OpsTemplateUtil;
import com.whoiszxl.core.utils.ShellUtil;
import com.whoiszxl.cqrs.dto.template.ZookeeperTemplateParamsEntity;
import com.whoiszxl.entity.Deploy;
import com.whoiszxl.entity.Server;
import com.whoiszxl.entity.Software;
import com.whoiszxl.entity.SoftwareConfig;
import com.whoiszxl.service.IScriptService;
import com.whoiszxl.service.IServerService;
import com.whoiszxl.service.ISoftwareConfigService;
import com.whoiszxl.strategy.InstallStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

@Slf4j
@Service
@RequiredArgsConstructor
public class Centos7InstallStrategy implements InstallStrategy {

    private final IServerService serverService;

    private final IScriptService scriptService;

    private final ISoftwareConfigService softwareConfigService;

    @Override
    public String viewFile(String absolutePath, Integer serverId) {
        return null;
    }

    @Override
    public boolean installJdk(Deploy deploy, Software software) {
        Vector<String> stdout = new Vector<>();

        String serverIds = deploy.getServerIds();
        String[] serverIdArr = serverIds.split(String.valueOf(CharPool.COMMA));
        List<Server> serverList = serverService.lambdaQuery().in(Server::getId, serverIdArr).list();

        StringBuilder sbShell = new StringBuilder();
        sbShell.append(software.getInstallScriptPath())
                .append(" '").append(software.getSoftwareFilename()).append("' '")
                .append(software.getSoftwarePath()).append("' '")
                .append(software.getInstallPath()).append("'");


        for (Server server : serverList) {
            ShellUtil shellUtil = new ShellUtil(server.getServerOuterIp(),
                    server.getServerUsername(),
                    server.getServerPassword(),
                    Integer.parseInt(server.getServerPort()));
            String logs = shellUtil.executeForResult(sbShell.toString());
            stdout.add(logs);
        }

        StringBuilder logs = new StringBuilder();
        for (String str : stdout) {
            logs.append(str);
        }

        return true;
    }

    @Override
    public boolean installZookeeper(Deploy deploy, Software software) {
        String serverIds = deploy.getServerIds();
        String[] serverIdArr = serverIds.split(String.valueOf(CharPool.COMMA));
        List<Server> serverList = serverService.lambdaQuery().in(Server::getId, serverIdArr).list();
        String clusterConfig = buildZkClusterConfig(serverList);

        int myId = 1;
        for (Server server : serverList) {
            ShellUtil shellUtil = new ShellUtil(server.getServerOuterIp(),
                    server.getServerUsername(),
                    server.getServerPassword(),
                    Integer.parseInt(server.getServerPort()));

            //1. 创建安装路径，并且设置 777 权限
            shellUtil.execute(String.format("mkdir %s && chmod -R 777 %s", software.getInstallPath(), software.getInstallPath()));

            //2. 解压到安装目录
            shellUtil.execute(String.format("tar -zxvf %s -C %s", software.getSoftwarePath() + software.getSoftwareFilename(), software.getInstallPath()));

            //3. 输出环境变量
            shellUtil.execute(String.format("echo '%s' > %s", OpsTemplateUtil.replaceGanR(software.getEnvContent()), software.getEnvPath()));

            //4. 解析zoo.cfg配置
            SoftwareConfig softwareConfig = softwareConfigService.getBySoftwareConfigName(SoftwareConfigNameConstants.ZOO_CFG);

            //4.1 解析模板参数
            ZookeeperTemplateParamsEntity templateParams = JSONUtil.toBean(softwareConfig.getConfigTemplateParams(), ZookeeperTemplateParamsEntity.class);

            //4.2 通过模板参数配置
            String zooCfg = OpsTemplateUtil.convertTemplate(softwareConfig.getConfigTemplate(), softwareConfig.getConfigTemplateParams());
            zooCfg = zooCfg + "\n" + clusterConfig;
            zooCfg = OpsTemplateUtil.replaceGanR(zooCfg);

            //4.3 将参数模板写入
            shellUtil.execute(String.format("echo '%s' > %s", zooCfg, softwareConfig.getConfigPath() + softwareConfig.getConfigName()));

            //5. 创建zk数据文件目录
            shellUtil.execute(String.format("mkdir %s", templateParams.getDataDir()));

            //6. 输出myId文件
            shellUtil.execute(String.format("echo %s > %s/myid", myId, templateParams.getDataDir()));

            myId++;
        }
        return true;
    }

    /**
     * 构建zk集群配置
     * @param servers
     * @return
     */
    private String buildZkClusterConfig(Collection<Server> servers) {
        int myid = 1;
        StringBuilder sb = new StringBuilder("#cluster\n");
        for (Server server : servers) {
            sb.append("server.");
            sb.append(myid);
            sb.append("=");
            sb.append(server.getServerName());
            sb.append(":2888:3888\n");
            myid++;
        }
        return sb.toString();
    }

    @Override
    public boolean installKafka(Deploy deploy, Software software) {
        String serverIds = deploy.getServerIds();
        String[] serverIdArr = serverIds.split(String.valueOf(CharPool.COMMA));
        List<Server> serverList = serverService.lambdaQuery().in(Server::getId, serverIdArr).list();

        int brokerId = 1;

        for (Server server : serverList) {
            ShellUtil shellUtil = new ShellUtil(server.getServerOuterIp(),
                    server.getServerUsername(),
                    server.getServerPassword(),
                    Integer.parseInt(server.getServerPort()));

            //1. 创建安装路径，并且设置 777 权限
            shellUtil.execute(String.format("mkdir %s && chmod -R 777 %s", software.getInstallPath(), software.getInstallPath()));

            //2. 解压到安装目录
            shellUtil.execute(String.format("tar -zxvf %s -C %s", software.getSoftwarePath() + software.getSoftwareFilename(), software.getInstallPath()));

            //3. 输出环境变量
            shellUtil.execute(String.format("echo '%s' >> %s", OpsTemplateUtil.replaceGanR(software.getEnvContent()), software.getEnvPath()));

            //4. 解析配置
            SoftwareConfig softwareConfig = softwareConfigService.getBySoftwareConfigName(SoftwareConfigNameConstants.KAFKA_SERVER_PROPERTIES);
            String kafkaConfig = OpsTemplateUtil.convertTemplate(softwareConfig.getConfigTemplate(), softwareConfig.getConfigTemplateParams());

            kafkaConfig = kafkaConfig + "\nbroker.id=" + brokerId;
            kafkaConfig = OpsTemplateUtil.replaceGanR(kafkaConfig);

            shellUtil.execute(String.format("echo '%s' > %s", kafkaConfig, softwareConfig.getConfigPath() + softwareConfig.getConfigName()));
            brokerId++;
        }

        return true;
    }

    @Override
    public boolean installHadoop(Deploy deploy, Software software) {
        return true;
    }

    @Override
    public boolean installFlume(Deploy deploy, Software software) {
        return true;
    }

    @Override
    public boolean installSpark(Deploy deploy, Software software) {
        return true;
    }

    @Override
    public boolean installEs(Deploy deploy, Software software) {
        String serverIds = deploy.getServerIds();
        String[] serverIdArr = serverIds.split(String.valueOf(CharPool.COMMA));
        List<Server> serverList = serverService.lambdaQuery().in(Server::getId, serverIdArr).list();


        String nodeName = "my_node_";
        int index = 1;
        for (Server server : serverList) {
            ShellUtil shellUtil = new ShellUtil(server.getServerOuterIp(),
                    server.getServerUsername(),
                    server.getServerPassword(),
                    Integer.parseInt(server.getServerPort()));

            //1. 创建安装路径，并且设置 777 权限
            shellUtil.execute(String.format("mkdir %s && chmod -R 777 %s", software.getInstallPath(), software.getInstallPath()));

            //2. 解压到安装目录
            shellUtil.execute(String.format("tar -zxvf %s -C %s", software.getSoftwarePath() + software.getSoftwareFilename(), software.getInstallPath()));

            //3. 输出环境变量
            shellUtil.execute(String.format("echo '%s' >> %s", OpsTemplateUtil.replaceGanR(software.getEnvContent()), software.getEnvPath()));

            //4.1 解析配置elasticsearch.yml，并动态添加node.name和cluster.initial_master_nodes
            SoftwareConfig softwareConfig = softwareConfigService.getBySoftwareConfigName(SoftwareConfigNameConstants.ES_ELASTICSEARCH_YML);
            String elasticSearchYmlConfig = OpsTemplateUtil.convertTemplate(softwareConfig.getConfigTemplate(), softwareConfig.getConfigTemplateParams());

            elasticSearchYmlConfig = elasticSearchYmlConfig + "\n" + "node.name: " + (nodeName + index);
            elasticSearchYmlConfig = elasticSearchYmlConfig + "\n" + "cluster.initial_master_nodes: " + buildESClusterConfig(nodeName, serverList.size());
            shellUtil.execute(String.format("echo '%s' >> %s", elasticSearchYmlConfig, softwareConfig.getConfigPath() + softwareConfig.getConfigName()));

            //4.2 解析jvm.options配置
            softwareConfig = softwareConfigService.getBySoftwareConfigName(SoftwareConfigNameConstants.ES_JVM_OPTIONS);
            String jvmOptionConfig = OpsTemplateUtil.convertTemplate(softwareConfig.getConfigTemplate(), softwareConfig.getConfigTemplateParams());
            shellUtil.execute(String.format("echo '%s' >> %s", jvmOptionConfig, softwareConfig.getConfigPath() + softwareConfig.getConfigName()));

            //fixme seed host此处有bug

            //5. 创建ES启动用户
            shellUtil.execute("useradd es && echo '123456' | passwd es --stdin");
            shellUtil.execute("source /etc/profile && sudo chown -R es $ES_HOME && sudo chmod -R 755 $ES_HOME");
            shellUtil.execute("echo 'vm.max_map_count=262144' | sudo tee -a /etc/sysctl.conf");
            shellUtil.execute("sudo -S sysctl -p");
            index++;
        }


        return true;
    }

    @Override
    public boolean installKibana(Deploy deploy, Software software) {
        String serverIds = deploy.getServerIds();
        String[] serverIdArr = serverIds.split(String.valueOf(CharPool.COMMA));
        List<Server> serverList = serverService.lambdaQuery().in(Server::getId, serverIdArr).list();


        String nodeName = "my_node_";
        int index = 1;
        for (Server server : serverList) {
            ShellUtil shellUtil = new ShellUtil(server.getServerOuterIp(),
                    server.getServerUsername(),
                    server.getServerPassword(),
                    Integer.parseInt(server.getServerPort()));

            //1. 创建安装路径，并且设置 777 权限
            shellUtil.execute(String.format("mkdir %s && chmod -R 777 %s", software.getInstallPath(), software.getInstallPath()));

            //2. 解压到安装目录
            shellUtil.execute(String.format("tar -zxvf %s -C %s", software.getSoftwarePath() + software.getSoftwareFilename(), software.getInstallPath()));

            //3. 输出环境变量
            shellUtil.execute(String.format("echo '%s' >> %s", OpsTemplateUtil.replaceGanR(software.getEnvContent()), software.getEnvPath()));

            //4 解析配置kibana.yml
            SoftwareConfig softwareConfig = softwareConfigService.getBySoftwareConfigName(SoftwareConfigNameConstants.ES_KIBANA_YML);
            String jvmOptionConfig = OpsTemplateUtil.convertTemplate(softwareConfig.getConfigTemplate(), softwareConfig.getConfigTemplateParams());
            shellUtil.execute(String.format("echo '%s' >> %s", jvmOptionConfig, softwareConfig.getConfigPath() + softwareConfig.getConfigName()));

            shellUtil.execute("source /etc/profile && sudo chown -R es $KIBANA_HOME && sudo chmod -R 755 $KIBANA_HOME");

        }

        return true;
    }

    /**
     * 构建zk集群配置
     * @param nodeNamePrefix 节点名称前缀
     * @param clusterNodeNum 集群节点数量
     * @return
     */
    private String buildESClusterConfig(String nodeNamePrefix, Integer clusterNodeNum) {

        StringBuilder sb = new StringBuilder("[");
        for (int i = 1; i <= clusterNodeNum; i++) {
            sb.append("\"").append(nodeNamePrefix).append(i).append("\"");
            if (i < clusterNodeNum) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
