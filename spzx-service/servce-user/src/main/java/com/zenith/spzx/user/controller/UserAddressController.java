package com.zenith.spzx.user.controller;

import com.zenith.spzx.model.entity.user.UserAddress;
import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.user.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/userAddress")
@Tag(name = "用户地址接口")
public class UserAddressController {
    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "获取用户地址列表")
    @GetMapping("auth/findUserAddressList")
    public Result<List<UserAddress>> findUserAddressList() {
        List<UserAddress> list = userInfoService.findUserAddressList();
        return Result.success(list);
    }
}
