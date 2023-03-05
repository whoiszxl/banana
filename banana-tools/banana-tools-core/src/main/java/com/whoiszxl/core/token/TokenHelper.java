package com.whoiszxl.core.token;

import com.whoiszxl.core.token.entity.LoginMember;

/**
 * token helper接口
 * @author whoiszxl
 */
public interface TokenHelper {

    /**
     * 登录
     * @param loginMember 登录用户信息
     */
    void login(LoginMember loginMember);

    /**
     * 获取当前登录用户信息
     * @return 登录用户信息
     */
    LoginMember getLoginMember();

    /**
     * 更新登录用户信息
     * @param loginMember 登录用户信息
     */
    void updateLoginMember(LoginMember loginMember);

    /**
     * 获取当前登录用户id
     * @return 用户id
     */
    Long getMemberId();

    /**
     * 获取当前登录用户名
     * @return 用户名
     */
    String getUsername();

}
