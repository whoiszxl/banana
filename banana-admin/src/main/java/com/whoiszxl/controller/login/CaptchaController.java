package com.whoiszxl.controller.login;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.IdUtil;
import com.wf.captcha.base.Captcha;
import com.whoiszxl.core.constants.RedisPrefixConstants;
import com.whoiszxl.core.entity.ResponseResult;
import com.whoiszxl.core.properties.CaptchaProperties;
import com.whoiszxl.core.utils.RedisUtils;
import com.whoiszxl.cqrs.response.ImageCaptchaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import static com.whoiszxl.core.constants.RedisPrefixConstants.Admin.ADMIN_CAPTCHA_IMAGE_KEY;

/**
 * @author whoiszxl
 */
@Tag(name = "验证码 API")
@SaIgnore
@RestController
@RequiredArgsConstructor
@RequestMapping("/captcha")
public class CaptchaController {

    private final RedisUtils redisUtils;

    private final CaptchaProperties captchaProperties;

    @SaIgnore
    @GetMapping("/image")
    @Operation(summary = "获取图片验证码", description = "获取一个Base64格式的验证码")
    public ResponseResult<ImageCaptchaResponse> getImageCaptcha() {
        //1. 生成图片验证码
        CaptchaProperties.CaptchaImage captchaImage = captchaProperties.getImage();
        Captcha captcha = captchaImage.getCaptcha();

        //2. 将验证码保存到Redis
        String uuid = IdUtil.fastSimpleUUID();
        String captchaKey = RedisPrefixConstants.format(ADMIN_CAPTCHA_IMAGE_KEY, uuid);
        redisUtils.setEx(captchaKey, captcha.text(), captchaImage.getExpirationInMinutes(), TimeUnit.MINUTES);

        //3. 返回结果
        return ResponseResult.buildSuccess(new ImageCaptchaResponse(uuid, captcha.toBase64()));
    }


}
