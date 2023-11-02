package com.zenith.spzx.user.service;

import com.zenith.spzx.model.dto.h5.UserLoginDto;
import com.zenith.spzx.model.dto.h5.UserRegisterDto;
import com.zenith.spzx.model.entity.user.UserAddress;
import com.zenith.spzx.model.vo.h5.UserInfoVo;

import java.util.List;

public interface UserInfoService {
    void register(UserRegisterDto userRegisterDto);

    String login(UserLoginDto userLoginDto);

    UserInfoVo getCurrentUserInfo();

    List<UserAddress> findUserAddressList();
}
