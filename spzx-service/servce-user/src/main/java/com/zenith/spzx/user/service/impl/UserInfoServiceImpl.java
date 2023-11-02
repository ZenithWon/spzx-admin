package com.zenith.spzx.user.service.impl;

import cn.hutool.db.Db;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.model.constant.RedisPathConstant;
import com.zenith.spzx.model.dto.h5.UserLoginDto;
import com.zenith.spzx.model.dto.h5.UserRegisterDto;
import com.zenith.spzx.model.entity.user.UserAddress;
import com.zenith.spzx.model.entity.user.UserInfo;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import com.zenith.spzx.model.vo.h5.UserInfoVo;
import com.zenith.spzx.user.mapper.UserAddressMapper;
import com.zenith.spzx.user.mapper.UserInfoMapper;
import com.zenith.spzx.user.service.UserInfoService;
import com.zenith.spzx.utils.AuthContextUtil;
import com.zenith.spzx.utils.UUIDUitils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public void register(UserRegisterDto userRegisterDto) {
        String code = userRegisterDto.getCode();
        String key= RedisPathConstant.USER_PHONE_CODE+userRegisterDto.getUsername();
        String dbCode = redisTemplate.opsForValue().get(key);

        String username=userRegisterDto.getUsername();
        String password=userRegisterDto.getPassword();
        String nickName=userRegisterDto.getNickName();

        if(StringUtils.isEmpty(dbCode)){
            throw new MyException(ResultCodeEnum.VALIDATECODE_EXPIRED);
        }
        if(!code.equals(dbCode)){
            throw new MyException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        UserInfo dbUserInfo = userInfoMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUsername , username)
        );
        if(dbUserInfo!=null){
            throw new MyException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }

        UserInfo userInfo=new UserInfo();
        userInfo.setUsername(username);
        userInfo.setNickName(nickName);
        userInfo.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        userInfo.setPhone(username);
        userInfo.setStatus(1);
        userInfo.setSex(0);
        userInfo.setAvatar("http://139.198.127.41:9000/sph/20230505/default_handsome.jpg");

        int res = userInfoMapper.insert(userInfo);
        if(res<=0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }

        redisTemplate.delete(key);
    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        String inputPassword=userLoginDto.getPassword();
        String username=userLoginDto.getUsername();

        UserInfo dbUserInfo = userInfoMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUsername , username)
        );

        if(dbUserInfo==null){
            throw new MyException(ResultCodeEnum.USER_NOT_EXIST);
        }

        String password= dbUserInfo.getPassword();
        inputPassword=DigestUtils.md5DigestAsHex(inputPassword.getBytes());
        if(!inputPassword.equals(password)){
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }

        String token = UUIDUitils.generateKey();
        redisTemplate.opsForValue().set(RedisPathConstant.USER_LOGIN+token, JSON.toJSONString(dbUserInfo),1, TimeUnit.DAYS);
        return token;
    }

    @Override
    public UserInfoVo getCurrentUserInfo() {
        UserInfo userInfo= AuthContextUtil.getUserInfo();
        UserInfoVo vo=new UserInfoVo();
        vo.setNickName(userInfo.getNickName());
        vo.setAvatar(userInfo.getAvatar());

        return vo;
    }

    @Override
    public List<UserAddress> findUserAddressList() {
        Long userId=AuthContextUtil.getUserInfo().getId();
        List<UserAddress> userAddressList = userAddressMapper.selectList(
                new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getUserId , userId)
        );
        return userAddressList;
    }

    @Override
    public UserAddress getById(Long id) {
        return userAddressMapper.selectById(id);
    }
}
