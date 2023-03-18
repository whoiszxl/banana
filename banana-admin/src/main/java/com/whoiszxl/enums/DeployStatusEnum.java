package com.whoiszxl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 部署初始化状态枚举
 * @author whoiszxl
 */
@Getter
@AllArgsConstructor
public enum DeployStatusEnum {

    /**
     * 已创建
     */
    CREATED(1),

    /**
     * 部署成功
     */
    DEPLOY_SUCCESS( 2),

    /**
     * 部署失败
     */
    DEPLOY_FAIL( 3),

    /**
     * 部署中
     */
    DEPLOYING( 4),
    ;

    private final Integer code;
}