package com.zenith.spzx.gateway.filter;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zenith.spzx.model.constant.RedisPathConstant;
import com.zenith.spzx.model.entity.user.UserInfo;
import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange , GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().getPath();
        String method=request.getMethod().name();

        log.debug("Request URL=> [{} {}]",method,url);

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if(antPathMatcher.match("/api/**/auth/**",url)){
            UserInfo userInfo = this.getUserInfo(request);
            if(userInfo==null){
                ServerHttpResponse response = exchange.getResponse();
                return out(response, ResultCodeEnum.LOGIN_AUTH);
            }
        }

        return chain.filter(exchange);
    }

    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        Result result = Result.build(null, resultCodeEnum);
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    private UserInfo getUserInfo(ServerHttpRequest request) {
        List<String> tokenList = request.getHeaders().get("token");
        if(CollectionUtil.isEmpty(tokenList)){
            return null;
        }
        String token=tokenList.get(0);
        if(StringUtils.isEmpty(token)){
            return null;
        }
        String userInfoJson = redisTemplate.opsForValue().get(RedisPathConstant.USER_LOGIN + token);
        if(StringUtils.isEmpty(userInfoJson)){
            return null;
        }
        UserInfo userInfo= JSON.parseObject(userInfoJson,UserInfo.class);
        return userInfo;

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
