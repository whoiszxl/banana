package com.whoiszxl.logger.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * log日志记录上下文信息
 * @author whoiszxl
 */
@Data
public class LogContext {

    private String createdBy;

    private LocalDateTime createdAt;

    private String errorMsg;

    private Exception exception;
}
