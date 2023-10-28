package com.zenith.spzx.manager.service;

import com.zenith.spzx.model.entity.system.SysMenu;

import java.util.List;

public interface SysMenuService {
    List<SysMenu> findNodes();

    void saveSysMenu(SysMenu sysMenu);

    void editSysMenu(SysMenu sysMenu);

    void deleteSysMenu(Long id);
}
