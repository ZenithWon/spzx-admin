package com.zenith.spzx.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.utils.AuthContextUtil;
import com.zenith.spzx.utils.UUIDUitils;
import com.zenith.spzx.manager.mapper.SysUserMapper;
import com.zenith.spzx.manager.service.SysUserService;
import com.zenith.spzx.model.constant.RedisPathConstant;
import com.zenith.spzx.model.dto.system.LoginDto;
import com.zenith.spzx.model.entity.system.SysUser;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import com.zenith.spzx.model.vo.system.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public LoginVo sysUserLogin(LoginDto dto) {
        String inputCode=dto.getCaptcha().toLowerCase();
        if(StringUtils.isEmpty(inputCode)){
            throw new MyException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        String username=dto.getUserName();
        String inputPassword=dto.getPassword();
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(inputPassword)){
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }
        String dbCode = (String) redisTemplate.opsForValue().get(RedisPathConstant.USER_VALIDATE_CODE+dto.getCodeKey());
        if(dbCode==null){
            throw new MyException(ResultCodeEnum.VALIDATECODE_EXPIRED);
        }

        if(!dbCode.equals(inputCode)){
            throw new MyException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        LambdaQueryWrapper<SysUser> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserName,username);
        SysUser sysUser=sysUserMapper.selectOne(wrapper);
        if(sysUser==null){
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }
        String dbPassword=sysUser.getPassword();
        inputPassword = DigestUtils.md5DigestAsHex(inputPassword.getBytes());
        if(!dbPassword.equals(inputPassword)){
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }

        String token= UUIDUitils.generateKey();
        redisTemplate.opsForValue().set(RedisPathConstant.USER_LOGIN +token, JSON.toJSONString(sysUser),30, TimeUnit.MINUTES);

        LoginVo loginVo=new LoginVo();
        loginVo.setToken(token);
        return loginVo;
    }

    @Override
    public SysUser getUserInfoByToken(String token) {
        String userJson= (String) redisTemplate.opsForValue().get(RedisPathConstant.USER_LOGIN+token);
        if(userJson==null){
            throw new MyException(ResultCodeEnum.LOGIN_AUTH);
        }
        SysUser sysUser=JSON.parseObject(userJson,SysUser.class);
        sysUser.setPassword(null);
        return sysUser;
    }

    @Override
    public Boolean sysUserLogout(String token) {
        return redisTemplate.delete(RedisPathConstant.USER_LOGIN + token);
    }
}
