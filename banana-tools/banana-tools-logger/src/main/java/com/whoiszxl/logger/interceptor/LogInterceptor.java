package com.whoiszxl.logger.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.whoiszxl.core.token.TokenHelper;
import com.whoiszxl.core.utils.BananaServletUtil;
import com.whoiszxl.core.utils.IpUtils;
import com.whoiszxl.logger.annotation.BananaLog;
import com.whoiszxl.logger.entity.AdminOptLog;
import com.whoiszxl.logger.entity.LogContext;
import com.whoiszxl.logger.enums.LogStatusEnum;
import com.whoiszxl.logger.holder.LogContextHolder;
import com.whoiszxl.logger.properties.LogProperties;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author whoiszxl
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenHelper tokenHelper;

    private final LogProperties logProperties;

    private static final String ENCRYPT_SYMBOL = "******";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if(!(handler instanceof HandlerMethod)) {
            return true;
        }
        BananaLog methodAnnotation = ((HandlerMethod) handler).getMethodAnnotation(BananaLog.class);
        if(methodAnnotation == null) {
            return true;
        }

        if(checkRecordEnabled(handler, request)) {
            LogContext logContext = new LogContext();
            logContext.setCreatedBy(tokenHelper.getUsername());
            logContext.setCreatedAt(LocalDateTime.now());
            LogContextHolder.set(logContext);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        //1. 从上下文中获取创建人，消耗时间，异常等信息
        LogContext logContext = LogContextHolder.get();
        AdminOptLog adminOptLog;
        if(logContext != null) {
            LogContextHolder.remove();
            adminOptLog = new AdminOptLog();
            adminOptLog.setStartTime(logContext.getCreatedAt());
            adminOptLog.setCreatedAt(logContext.getCreatedAt());
            LocalDateTime now = LocalDateTime.now();
            adminOptLog.setFinishTime(now);
            adminOptLog.setConsumingTime(LocalDateTimeUtil.toEpochMilli(now) - LocalDateTimeUtil.toEpochMilli(logContext.getCreatedAt()));
            adminOptLog.setStatus(LogStatusEnum.SUCCESS.getCode());

            String errorMsg = logContext.getErrorMsg();
            if(StrUtil.isNotBlank(errorMsg)) {
                adminOptLog.setStatus(LogStatusEnum.FAIL.getCode());
                adminOptLog.setExDesc(errorMsg);
            }

            Exception exception = logContext.getException();
            if(exception != null) {
                adminOptLog.setStatus(LogStatusEnum.FAIL.getCode());
                adminOptLog.setExDetail(ExceptionUtil.stacktraceToString(exception, -1));
            }

        }else {
            return;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;


        //2. 记录方法信息
        String methodName = handlerMethod.getMethod().getName();
        adminOptLog.setActionMethod(methodName);
        adminOptLog.setClassPath(methodName);

        //3. 记录描述
        Operation descOperation = handlerMethod.getMethodAnnotation(Operation.class);
        BananaLog methodLog = handlerMethod.getMethodAnnotation(BananaLog.class);

        if(descOperation != null) {
            adminOptLog.setDescription(
                    StrUtil.blankToDefault(descOperation.summary(), String.format("%s 方法上没有描述", methodName)));
        }

        if(methodLog != null && StrUtil.isNotBlank(methodLog.value())) {
            adminOptLog.setDescription(methodLog.value());
        }

        //4. 记录request信息
        adminOptLog.setRequestUri(StrUtil.isBlank(request.getQueryString())
                ? request.getRequestURL().toString()
                : request.getRequestURL().append("?").append(request.getQueryString()).toString());
        adminOptLog.setRequestHttpMethod(request.getMethod());
        adminOptLog.setRequestHeaders(this.desensitize(ServletUtil.getHeaderMap(request)));
        String requestBody = this.getRequestBody(request);
        if (StrUtil.isNotBlank(requestBody)) {
            adminOptLog.setRequestBody(this.desensitize(
                    JSONUtil.isTypeJSON(requestBody) ? JSONUtil.parseObj(requestBody) : ServletUtil.getParamMap(request)));
        }
        adminOptLog.setRequestIp(ServletUtil.getClientIP(request));
        adminOptLog.setUa(BananaServletUtil.getBrowser(request));
        adminOptLog.setCreatedBy(ObjectUtil.defaultIfNull(logContext.getCreatedBy(), tokenHelper.getUsername()));



        //5. 记录response信息
        int status = response.getStatus();
        adminOptLog.setStatusCode(status);
        adminOptLog.setResponseHeader(this.desensitize(ServletUtil.getHeadersMap(response)));

        String responseBody = this.getResponseBody(response);
        if (StrUtil.isNotBlank(responseBody) && JSONUtil.isTypeJSON(responseBody)) {
            adminOptLog.setResponseResult(responseBody);
        }
        adminOptLog.setStatus(status >= HttpStatus.HTTP_BAD_REQUEST ? LogStatusEnum.FAIL.getCode() : adminOptLog.getStatus());

        SpringUtil.getApplicationContext().publishEvent(adminOptLog);
    }

    private String getResponseBody(HttpServletResponse response) {
        String responseBody = "";
        ContentCachingResponseWrapper wrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            responseBody = StrUtil.utf8Str(wrapper.getContentAsByteArray());
        }
        return responseBody;
    }

    private String getRequestBody(HttpServletRequest request) {
        String requestBody = "";
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            requestBody = StrUtil.utf8Str(wrapper.getContentAsByteArray());
        }
        return requestBody;
    }

    private String desensitize(Map headerMap) {
        String desensitizeDataStr = JSONUtil.toJsonStr(headerMap);
        try {
            if (CollUtil.isEmpty(headerMap)) {
                return desensitizeDataStr;
            }

            for (String desensitizeProperty : logProperties.getDesensitize()) {
                headerMap.computeIfPresent(desensitizeProperty, (k, v) -> ENCRYPT_SYMBOL);
                headerMap.computeIfPresent(desensitizeProperty.toLowerCase(), (k, v) -> ENCRYPT_SYMBOL);
                headerMap.computeIfPresent(desensitizeProperty.toUpperCase(), (k, v) -> ENCRYPT_SYMBOL);
            }
            return JSONUtil.toJsonStr(headerMap);
        } catch (Exception ex) {
        }
        return desensitizeDataStr;
    }

    private boolean checkRecordEnabled(Object handler, HttpServletRequest request) {
        if(!(handler instanceof HandlerMethod) || !logProperties.getEnabled()) {
            return false;
        }


        boolean isInnerIp = IpUtils.isInnerIp(ServletUtil.getClientIP(request));
        if(isInnerIp && !logProperties.getIncludeInnerIp()) {
            return false;
        }

        HandlerMethod handlerMethod = (HandlerMethod)handler;
        BananaLog bananaLog = handlerMethod.getMethodAnnotation(BananaLog.class);
        if(logProperties.getExcludeMethods().contains(request.getMethod()) && bananaLog != null) {
            return false;
        }

        Operation operation = handlerMethod.getMethodAnnotation(Operation.class);
        if(bananaLog == null && operation == null) {
            return false;
        }

        if(operation != null && operation.hidden()) {
            return false;
        }

        return bananaLog == null || !bananaLog.ignore();
    }
}
