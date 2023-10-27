package com.zenith.spzx.manager.service;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.manager.mapper.SysUserMapper;
import com.zenith.spzx.model.dto.system.LoginDto;
import com.zenith.spzx.model.dto.system.SysUserDto;
import com.zenith.spzx.model.entity.system.SysUser;
import com.zenith.spzx.model.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;

public interface SysUserService {
    LoginVo sysUserLogin(LoginDto dto);

    SysUser getUserInfoByToken(String token);

    Boolean sysUserLogout(String token);

    PageInfo<SysUser> pageQuerySysUser(Integer pageNum , Integer pageSize , SysUserDto dto);

    void saveSysUser(SysUser sysUser);

    void editSysUser(SysUser sysUser);

    void deleteSysUserById(Long id);
}
