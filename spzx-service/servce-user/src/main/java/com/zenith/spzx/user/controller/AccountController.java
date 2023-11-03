package com.zenith.spzx.user.controller;

import com.zenith.spzx.model.dto.h5.UserLoginDto;
import com.zenith.spzx.model.dto.h5.UserRegisterDto;
import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.model.vo.h5.UserInfoVo;
import com.zenith.spzx.user.service.UserInfoService;
import com.zenith.spzx.utils.AuthContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "会员用户接口")
@RestController
@RequestMapping("api/user/userInfo")
public class AccountController {
    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "会员注册")
    @PostMapping("/register")
    public Result<Object> register(@RequestBody UserRegisterDto userRegisterDto) {
        userInfoService.register(userRegisterDto);
        return Result.success(null) ;
    }

    @Operation(summary = "会员登录")
    @PostMapping("/login")
    public Result<Object> login(@RequestBody UserLoginDto userLoginDto) {
        return Result.success(userInfoService.login(userLoginDto));
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("auth/getCurrentUserInfo")
    public Result<UserInfoVo> getCurrentUserInfo() {
        UserInfoVo userInfoVo = userInfoService.getCurrentUserInfo();
        return Result.success(userInfoVo) ;
    }
}
