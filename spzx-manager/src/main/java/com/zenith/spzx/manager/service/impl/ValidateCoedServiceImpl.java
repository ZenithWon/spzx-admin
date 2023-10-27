package com.zenith.spzx.manager.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.zenith.spzx.utils.UUIDUitils;
import com.zenith.spzx.manager.service.ValidateCodeService;
import com.zenith.spzx.model.constant.RedisPathConstant;
import com.zenith.spzx.model.vo.system.ValidateCodeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ValidateCoedServiceImpl implements ValidateCodeService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ValidateCodeVo generateValidataCode() {
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 48, 4, 20);
        String codeValue=circleCaptcha.getCode();
        String image=circleCaptcha.getImageBase64();

        String key= UUIDUitils.generateKey();
        redisTemplate.opsForValue().set(RedisPathConstant.USER_VALIDATE_CODE +key,codeValue.toLowerCase(),5, TimeUnit.MINUTES);

        ValidateCodeVo validateCodeVo=new ValidateCodeVo();
        validateCodeVo.setCodeKey(key);
        validateCodeVo.setCodeValue("data:image/png;base64,"+image);
        return validateCodeVo;
    }
}
