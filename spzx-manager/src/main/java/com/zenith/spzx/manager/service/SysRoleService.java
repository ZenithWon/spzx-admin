package com.zenith.spzx.manager.service;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.model.dto.system.SysRoleDto;
import com.zenith.spzx.model.entity.system.SysRole;

import java.util.Map;

public interface SysRoleService {
    PageInfo<SysRole> queryRole(SysRoleDto sysRoleDto , Integer current , Integer limit);

    void saveSysRole(SysRole sysRole);

    void editSysRole(SysRole sysRole);

    void deleteSysRole(Long id);

    Map<String,Object> findAll(Long userId);
}
