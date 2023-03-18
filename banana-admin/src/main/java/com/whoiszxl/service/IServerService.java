package com.whoiszxl.service;

import com.whoiszxl.cqrs.command.ConnectTestCommand;
import com.whoiszxl.entity.Server;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务器表 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
public interface IServerService extends IService<Server> {

    /**
     * 服务器连接测试
     * @param command 服务器连接测试命令
     * @return 是否连接成功
     */
    boolean connectTest(ConnectTestCommand command);
}
