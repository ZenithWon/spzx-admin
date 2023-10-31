package com.zenith.spzx.manager.interceptor;

import com.alibaba.fastjson.JSON;
import com.zenith.spzx.model.constant.RedisPathConstant;
import com.zenith.spzx.model.entity.system.SysUser;
import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import com.zenith.spzx.utils.AuthContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class LoginAuthInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request , HttpServletResponse response , Object handler) throws Exception {
        String method = request.getMethod();

        if(method.equals("OPTIONS")){
            return true;
        }

        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            responseNoLoginInfo(response);
            return false;
        }

        String userInfo = (String)redisTemplate.opsForValue().get(RedisPathConstant.USER_LOGIN + token);

        if(StringUtils.isEmpty(userInfo)){
            responseNoLoginInfo(response);
            return false;
        }

        SysUser sysUser=JSON.parseObject(userInfo,SysUser.class);
        AuthContextUtil.set(sysUser);

        redisTemplate.expire(RedisPathConstant.USER_LOGIN+token,30, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request , HttpServletResponse response , Object handler , Exception ex) throws Exception {
        AuthContextUtil.remove();

    }

    private void responseNoLoginInfo(HttpServletResponse response) {
        Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }

}
