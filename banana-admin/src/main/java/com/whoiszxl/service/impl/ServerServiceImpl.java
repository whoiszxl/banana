package com.whoiszxl.service.impl;

import com.whoiszxl.core.utils.ShellUtil;
import com.whoiszxl.cqrs.command.ConnectTestCommand;
import com.whoiszxl.entity.Server;
import com.whoiszxl.mapper.ServerMapper;
import com.whoiszxl.service.IServerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器表 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
@Service
public class ServerServiceImpl extends ServiceImpl<ServerMapper, Server> implements IServerService {

    @Override
    public boolean connectTest(ConnectTestCommand command) {
        ShellUtil shellUtil = new ShellUtil(
                command.getServerOuterIp(),
                command.getServerUsername(),
                command.getServerPassword(),
                Integer.parseInt(command.getServerPort()));
        try{
            return shellUtil.execute("ls /") == 0;
        }finally {
            shellUtil.close();
        }
    }
}
