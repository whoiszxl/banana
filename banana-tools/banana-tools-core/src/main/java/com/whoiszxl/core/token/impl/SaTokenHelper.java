package com.whoiszxl.core.token.impl;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.whoiszxl.core.constants.LoginConstants;
import com.whoiszxl.core.token.TokenHelper;
import com.whoiszxl.core.token.entity.LoginMember;
import com.whoiszxl.core.utils.BananaServletUtil;
import com.whoiszxl.core.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * sa-token的helper
 * @author whoiszxl
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "banana.token-active", havingValue = "sa-token")
public class SaTokenHelper implements TokenHelper {


    @Override
    public void login(LoginMember loginMember) {
        if(loginMember == null) {
            return;
        }

        HttpServletRequest request = BananaServletUtil.getRequest();
        loginMember.setIp(ServletUtil.getClientIP(request));
        loginMember.setLocation(IpUtils.getCityInfo(loginMember.getIp()));
        loginMember.setBrowser(BananaServletUtil.getBrowser(request));

        StpUtil.login(loginMember.getId());
        loginMember.setToken(StpUtil.getTokenValue());
        SaHolder.getStorage().set(LoginConstants.LOGIN_MEMBER_KEY, loginMember);
        StpUtil.getTokenSession().set(LoginConstants.LOGIN_MEMBER_KEY, loginMember);
    }

    @Override
    public LoginMember getLoginMember() {
        LoginMember loginMember = (LoginMember) SaHolder.getStorage().get(LoginConstants.LOGIN_MEMBER_KEY);
        if(loginMember != null) {
            return loginMember;
        }
        loginMember = (LoginMember) StpUtil.getTokenSession().get(LoginConstants.LOGIN_MEMBER_KEY);
        SaHolder.getStorage().set(LoginConstants.LOGIN_MEMBER_KEY, loginMember);

        return loginMember;
    }

    @Override
    public void updateLoginMember(LoginMember loginMember) {
        SaHolder.getStorage().set(LoginConstants.LOGIN_MEMBER_KEY, loginMember);
        StpUtil.getTokenSession().set(LoginConstants.LOGIN_MEMBER_KEY, loginMember);
    }

    @Override
    public Long getMemberId() {
        return getLoginMember().getId();
    }

    @Override
    public String getUsername() {
        return getLoginMember().getUsername();
    }
}
