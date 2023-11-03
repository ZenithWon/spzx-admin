package com.zenith.spzx.user.service.impl;

import com.zenith.spzx.model.constant.RedisPathConstant;
import com.zenith.spzx.user.service.SmsService;
import com.zenith.spzx.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {
    @Value("${sms.AppCode}")
    private String AppCode;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void sendCode(String phone) {
        String key= RedisPathConstant.USER_PHONE_CODE+phone;
        String code = redisTemplate.opsForValue().get(key);
        if(!StringUtils.isEmpty(code)){
            log.debug("Sms has been sent code=> {}",code);
            return;
        }
        code= RandomStringUtils.randomNumeric(4);
        sendSms(phone,code);
        redisTemplate.opsForValue().set(key,code,5, TimeUnit.MINUTES);
        log.debug("Sms sending completed  code=> {}",code);
    }

    private void sendSms(String phone,String code){
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + AppCode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        querys.put("param", "**code**:"+code+",**minute**:5");

        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
