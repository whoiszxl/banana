package com.whoiszxl.logger.holder;

import com.whoiszxl.logger.entity.LogContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author whoiszxl
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogContextHolder {

    private static final ThreadLocal<LogContext> LOG_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static void set(LogContext logContext) {
        LOG_CONTEXT_THREAD_LOCAL.set(logContext);
    }

    public static LogContext get() {
        return LOG_CONTEXT_THREAD_LOCAL.get();
    }

    public static void remove() {
        LOG_CONTEXT_THREAD_LOCAL.remove();
    }

    public static void setException(Exception e) {
        LogContext logContext = get();
        if (logContext != null) {
            logContext.setException(e);
        }
    }

    public static void setErrorMsg(String errorMsg) {
        LogContext logContext = get();
        if (logContext != null) {
            logContext.setErrorMsg(errorMsg);
        }
    }
}
