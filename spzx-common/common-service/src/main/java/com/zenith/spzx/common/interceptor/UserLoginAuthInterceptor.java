package com.zenith.spzx.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.zenith.spzx.model.constant.RedisPathConstant;
import com.zenith.spzx.model.entity.user.UserInfo;
import com.zenith.spzx.utils.AuthContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserLoginAuthInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request , HttpServletResponse response , Object handler) throws Exception {
        String token=request.getHeader("token");
        String userInfoJson = redisTemplate.opsForValue().get(RedisPathConstant.USER_LOGIN + token);

        AuthContextUtil.setUserInfo(JSON.parseObject(userInfoJson, UserInfo.class));

        return true;
    }
}
