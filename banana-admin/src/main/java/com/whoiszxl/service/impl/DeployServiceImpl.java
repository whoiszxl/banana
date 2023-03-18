package com.whoiszxl.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.StrPool;
import com.whoiszxl.constants.ScriptConstants;
import com.whoiszxl.constants.SoftwareConstants;
import com.whoiszxl.core.exception.ExceptionCatcher;
import com.whoiszxl.core.utils.ShellUtil;
import com.whoiszxl.entity.Deploy;
import com.whoiszxl.entity.Script;
import com.whoiszxl.entity.Server;
import com.whoiszxl.entity.Software;
import com.whoiszxl.enums.DeployInitStatusEnum;
import com.whoiszxl.enums.DeployStatusEnum;
import com.whoiszxl.mapper.DeployMapper;
import com.whoiszxl.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whoiszxl.strategy.InstallStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务部署表 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-13
 */
@Service
@RequiredArgsConstructor
public class DeployServiceImpl extends ServiceImpl<DeployMapper, Deploy> implements IDeployService {

    private final IServerService serverService;

    private final ISoftwareService softwareService;

    private final IScriptService scriptService;

    private final InstallStrategy installStrategy;

    @Override
    public boolean deploy(Integer deployId) {
        Deploy deploy = this.getById(deployId);
        List<Server> serverList = getServerList(deploy);
        for (Server server : serverList) {
            ShellUtil shellUtil = new ShellUtil(server.getServerOuterIp(),
                    server.getServerUsername(),
                    server.getServerPassword(),
                    Integer.parseInt(server.getServerPort()));
            int execute = shellUtil.execute("ls /");
            shellUtil.close();
            Assert.isTrue(execute == 0, server.getServerOuterIp() + "服务实例无法连接");
        }

        //2. 初始化软件环境
        Software software = softwareService.getById(deploy.getSoftwareId());

        this.updateStatusById(deploy.getId(), DeployStatusEnum.DEPLOYING);
        boolean installFlag = false;
        switch (software.getSoftwareName()) {
            case SoftwareConstants.JDK:
                installFlag = installStrategy.installJdk(deploy, software);
                break;
            case SoftwareConstants.ZOOKEEPER:
                installFlag = installStrategy.installZookeeper(deploy, software);
                break;
            case SoftwareConstants.KAFKA:
                installFlag = installStrategy.installKafka(deploy, software);
                break;
            case SoftwareConstants.HADOOP:
                installFlag = installStrategy.installHadoop(deploy, software);
                break;
            case SoftwareConstants.FLUME:
                installFlag = installStrategy.installFlume(deploy, software);
                break;
            case SoftwareConstants.SPARK:
                installFlag = installStrategy.installSpark(deploy, software);
                break;
            case SoftwareConstants.ELASTICSEARCH:
                installFlag = installStrategy.installEs(deploy, software);
                break;

            case SoftwareConstants.KIBANA:
                installFlag = installStrategy.installKibana(deploy, software);
                break;
            default:
                break;
        }
        if(!installFlag) {
            this.updateStatusById(deploy.getId(), DeployStatusEnum.DEPLOY_FAIL);
            ExceptionCatcher.catchServiceEx("安装失败，请排查失败原因");
        }

        this.updateStatusById(deploy.getId(), DeployStatusEnum.DEPLOY_SUCCESS);

        return installFlag;
    }

    /**
     * 通过id更新状态
     * @param id id
     * @param statusEnum 状态
     */
    private void updateStatusById(Integer id, DeployStatusEnum statusEnum) {
        Deploy deploy = new Deploy();
        deploy.setId(id);
        deploy.setStatus(statusEnum.getCode());
        this.updateById(deploy);
    }


    @Override
    public boolean init(Integer deployId) {
        Deploy deploy = this.getById(deployId);
        List<Server> serverList = getServerList(deploy);

        StringBuilder hostSb = new StringBuilder();
        StringBuilder sshNoPassSb = new StringBuilder();
        for (Server server : serverList) {
            hostSb.append(server.getServerInnerIp()).append(" ").append(server.getServerName()).append(StrPool.LF);
            sshNoPassSb.append(server.getServerName()).append("~").append(server.getServerPassword()).append("|");
        }
        sshNoPassSb.deleteCharAt(sshNoPassSb.length() - 1);

        StringBuilder logs = new StringBuilder();
        for (Server server : serverList) {
            //1. 修改host
            ShellUtil shellUtil = new ShellUtil(server.getServerOuterIp(),
                    server.getServerUsername(),
                    server.getServerPassword(),
                    Integer.parseInt(server.getServerPort()));

            shellUtil.execute("echo -e '" + hostSb + "' >> /etc/hosts");

            // 2. 关闭防火墙
            shellUtil.execute("systemctl stop firewalld.service &&systemctl disable firewalld.service");

            //3. 脚本同步
            List<Script> scriptList = scriptService.list();
            for (Script script : scriptList) {
                shellUtil.execute("mkdir -p " + script.getScriptPath());
                shellUtil.executeForResult("echo '" + script.getScriptContent() + "' > " + script.getScriptPath() + StrPool.SLASH + script.getScriptName());
                shellUtil.execute("chmod +x " + script.getScriptPath() + StrPool.SLASH +  script.getScriptName());
            }

            String scriptHome = "#SCRIPT_HOME\n" +
                    "export SCRIPT_HOME=/opt/banana/shell\n" +
                    "export PATH=$PATH:$SCRIPT_HOME";
            shellUtil.execute(String.format("echo '%s' > /etc/profile.d/ops_script_env.sh", scriptHome));

            // 4. 配置SSH免密登录
            Script script = scriptService.lambdaQuery().eq(Script::getScriptName, ScriptConstants.SSH_NO_PASS_LOGIN).one();
            String sshNoLoginResult = shellUtil.executeForResult("sh " + script.getScriptPath() + StrPool.SLASH + script.getScriptName() + " '" + sshNoPassSb + "'");
            logs.append("######## ").append(server.getServerName()).append(" ########\n").append(sshNoLoginResult);
        }

        Deploy updateDeploy = new Deploy();
        updateDeploy.setId(deployId);
        updateDeploy.setDeployLogs(logs.toString());
        updateDeploy.setInitStatus(DeployInitStatusEnum.INITIALIZED.getCode());
        this.updateById(updateDeploy);
        return true;
    }


    @Override
    public String start(Integer deployId) {
        Deploy deploy = this.getById(deployId);
        List<Server> serverList = getServerList(deploy);
        String params = serverList.stream().map(Server::getServerName).collect(Collectors.joining(" "));
        Server server = serverList.get(0);
        ShellUtil shellUtil = new ShellUtil(server.getServerOuterIp(),
                server.getServerUsername(),
                server.getServerPassword(),
                Integer.parseInt(server.getServerPort()));

        Software software = softwareService.getById(deploy.getSoftwareId());

        return shellUtil.executeForResult(String.format("%s '%s'", software.getStartScriptPath(), params));
    }

    @Override
    public String status(Integer deployId) {
        Deploy deploy = this.getById(deployId);
        List<Server> serverList = getServerList(deploy);
        String params = serverList.stream().map(Server::getServerName).collect(Collectors.joining(" "));

        Server server = serverList.get(0);
        ShellUtil shellUtil = new ShellUtil(server.getServerOuterIp(),
                server.getServerUsername(),
                server.getServerPassword(),
                Integer.parseInt(server.getServerPort()));

        Software software = softwareService.getById(deploy.getSoftwareId());

        return shellUtil.executeForResult(String.format("%s '%s'", software.getStatusScriptPath(), params));
    }

    @Override
    public boolean syncSoftware(Integer deployId) {
        Deploy deploy = this.getById(deployId);
        List<Server> serverList = getServerList(deploy);
        String params = serverList.stream().map(Server::getServerName).collect(Collectors.joining(" "));

        Server server = serverList.get(0);
        ShellUtil shellUtil = new ShellUtil(server.getServerOuterIp(),
                server.getServerUsername(),
                server.getServerPassword(),
                Integer.parseInt(server.getServerPort()));
        List<Software> softwareList = softwareService.list();
        for (Software software : softwareList) {
            shellUtil.execute(String.format("%s '%s' '%s'", "xsync.sh", params, software.getSoftwarePath()));
        }
        return true;
    }

    private List<Server> getServerList(Deploy deploy) {
        //1. 校验机器是否能够联通
        String serverIds = deploy.getServerIds();
        String[] serverIdArr = serverIds.split(String.valueOf(CharPool.COMMA));
        return serverService.lambdaQuery().in(Server::getId, serverIdArr).list();
    }
}
