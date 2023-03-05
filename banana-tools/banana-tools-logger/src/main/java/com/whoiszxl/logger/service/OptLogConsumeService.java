package com.whoiszxl.logger.service;


import com.whoiszxl.logger.entity.AdminOptLog;
import com.whoiszxl.logger.mapper.AdminOptLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 日志异步保存服务
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class OptLogConsumeService {

    private final AdminOptLogMapper adminOptLogMapper;

    @Async
    @EventListener
    public void saveToDb(AdminOptLog adminOptLog) {
        adminOptLogMapper.insert(adminOptLog);
    }

}
