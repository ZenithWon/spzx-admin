package com.zenith.spzx.common.log.aspect;

import com.alibaba.fastjson.JSON;
import com.zenith.spzx.common.log.annotation.Log;
import com.zenith.spzx.common.log.service.LogService;
import com.zenith.spzx.model.entity.system.SysOperLog;
import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.utils.AuthContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @Autowired
    private LogService logService;

    @Around("@annotation(sysLog)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint,Log sysLog){
        SysOperLog sysOperLog=getLogObj(joinPoint,sysLog);
        Object proceed=null;

        long begin = System.currentTimeMillis();
        try {
            log.debug("Request begin => URL[{} {}]",sysOperLog.getRequestMethod(),sysOperLog.getOperUrl());
            proceed= joinPoint.proceed();

            sysOperLog.setStatus(0);
            sysOperLog.setErrorMsg(null);
        } catch (Throwable throwable) {
            sysOperLog.setStatus(1);
            sysOperLog.setErrorMsg(throwable.getMessage());
            throw new RuntimeException(throwable);

        } finally {
            long end = System.currentTimeMillis();
            log.debug("Request end => URL[{} {}], consume time: {}ms",sysOperLog.getRequestMethod(),sysOperLog.getOperUrl(),end-begin);
            logService.saveSysOperLog(sysOperLog);
        }

        return proceed;
    }

    private SysOperLog getLogObj(ProceedingJoinPoint joinPoint,Log sysLog){
        SysOperLog sysOperLog=new SysOperLog();

        MethodSignature signature=(MethodSignature)joinPoint.getSignature();
        sysOperLog.setMethod(signature.getName());

        sysOperLog.setTitle(sysLog.title());
        sysOperLog.setOperatorType(sysLog.operatorType().toString());
        sysOperLog.setBusinessType(sysLog.businessType().toString());

        ServletRequestAttributes ra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ra.getRequest();
        sysOperLog.setRequestMethod(request.getMethod());
        sysOperLog.setOperUrl(request.getRequestURI());
        sysOperLog.setOperIp(getRemoteHost(request));
        sysOperLog.setOperName(AuthContextUtil.get().getName());

        return sysOperLog;

    }

    private String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
