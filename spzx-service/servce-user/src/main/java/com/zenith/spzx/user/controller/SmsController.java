package com.zenith.spzx.user.controller;

import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.user.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user/sms")
public class SmsController {
    @Autowired
    private SmsService smsService;

    @GetMapping(value = "/sendCode/{phone}")
    public Result sendCode(@PathVariable String phone){
        smsService.sendCode(phone);
        return Result.success(null);
    }
}
