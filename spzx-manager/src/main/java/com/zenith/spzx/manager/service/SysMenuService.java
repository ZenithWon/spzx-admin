package com.zenith.spzx.manager.service;

import com.zenith.spzx.model.entity.system.SysMenu;
import com.zenith.spzx.model.vo.system.SysMenuVo;

import java.util.List;
import java.util.Map;

public interface SysMenuService {
    List<SysMenu> findNodes();

    void saveSysMenu(SysMenu sysMenu);

    void editSysMenu(SysMenu sysMenu);

    void deleteSysMenu(Long id);

    Map<String, Object> findSysRoleMenuByRoleId(Long roleId);

    List<SysMenuVo> findMenuByUserId();
}
