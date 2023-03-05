package com.whoiszxl.controller.login;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.lang.Assert;
import com.whoiszxl.core.constants.RedisPrefixConstants;
import com.whoiszxl.core.entity.ResponseResult;
import com.whoiszxl.core.utils.RedisUtils;
import com.whoiszxl.core.utils.SecureUtils;
import com.whoiszxl.cqrs.command.LoginByPasswordCommand;
import com.whoiszxl.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.whoiszxl.core.constants.RedisPrefixConstants.Admin.ADMIN_CAPTCHA_IMAGE_KEY;

/**
 * @author whoiszxl
 */
@Tag(name = "登录相关接口")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisUtils redisUtils;

    @SaIgnore
    @Operation(summary = "管理员后台登录", description = "根据管理员用户名和密码进行登录")
    @PostMapping("/password")
    public ResponseResult<String> loginByPassword(@Validated @RequestBody LoginByPasswordCommand command) {
        //1. 验证码校验
        String captchaKey = RedisPrefixConstants.format(ADMIN_CAPTCHA_IMAGE_KEY, command.getUuid());
        String captchaValue = redisUtils.get(captchaKey);
        Assert.notBlank(captchaValue, "验证码不存在");
        Assert.equals(captchaValue, command.getCaptcha(), "验证码错误");

        //2. 登录
        String password = SecureUtils.decryptByRsaPrivateKey(command.getPassword());
        Assert.notBlank(password, "密码错误");

        String token = loginService.login(command.getUsername(), password);
        return ResponseResult.buildSuccess(token);
    }





}
