package com.whoiszxl.controller.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.whoiszxl.core.constants.LoginConstants;
import com.whoiszxl.core.entity.PageQuery;
import com.whoiszxl.core.entity.ResponseResult;
import com.whoiszxl.core.entity.response.PageResponse;
import com.whoiszxl.core.token.entity.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author whoiszxl
 */
@Tag(name = "在线管理员 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/monitor/online")
public class OnlineController {

    @PostMapping("/page")
    @SaCheckPermission(value = {"monitor:online:list"})
    @Operation(summary = "管理后台新增管理员", description = "管理后台新增管理员")
    public ResponseResult<PageResponse<LoginMember>> onlinePageList(@Validated @RequestBody PageQuery pageQuery) {
        List<LoginMember> loginMemberList = new ArrayList<>();

        List<String> tokenKeyList = StpUtil.searchTokenValue("", 0, -1, false);
        tokenKeyList.removeIf(tokenKey -> StpUtil.stpLogic.getTokenActivityTimeoutByToken(StrUtil.subAfter(tokenKey, ":", true)) < SaTokenDao.NEVER_EXPIRE);
        loginMemberList = tokenKeyList.stream().map(tokenKey -> {
            String token = StrUtil.subAfter(tokenKey, ":", true);
            SaSession saSession = StpUtil.getTokenSessionByToken(token);
            return saSession.get(LoginConstants.LOGIN_MEMBER_KEY, new LoginMember());
        }).collect(Collectors.toList());

        CollUtil.sort(loginMemberList, Comparator.comparing(LoginMember::getLastLoginTime).reversed());
        PageResponse<LoginMember> loginMemberPage = PageResponse.convert(pageQuery.getPage(), pageQuery.getSize(), loginMemberList);

        return ResponseResult.buildSuccess(loginMemberPage);
    }
}
