package com.zenith.spzx.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.manager.mapper.SysMenuMapper;
import com.zenith.spzx.manager.mapper.SysRoleMenuMapper;
import com.zenith.spzx.manager.service.SysMenuService;
import com.zenith.spzx.model.entity.system.SysMenu;
import com.zenith.spzx.model.entity.system.SysRoleMenu;
import com.zenith.spzx.model.entity.system.SysUser;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import com.zenith.spzx.model.vo.system.SysMenuVo;
import com.zenith.spzx.utils.AuthContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    private List<SysMenu> allSysMenu;

    List<SysMenu> dfsFindNodes(SysMenu sysMenu){
        SysMenu findObj=new SysMenu();
        findObj.setParentId(sysMenu.getId());

        List<SysMenu> parentMenus=new ArrayList<>();
        while(allSysMenu.contains(findObj)){
            Integer index=allSysMenu.indexOf(findObj);
            SysMenu parent=allSysMenu.get(index);
            List<SysMenu> children = dfsFindNodes(parent);
            parent.setChildren(children);
            parentMenus.add(parent);
            allSysMenu.remove(findObj);
        }
        return parentMenus;
    }

    @Override
    public List<SysMenu> findNodes() {
        this.allSysMenu=sysMenuMapper.selectList(null);
        SysMenu rootNode=new SysMenu();
        rootNode.setId(0L);

        return dfsFindNodes(rootNode);
    }

    @Override
    @Transactional
    public void saveSysMenu(SysMenu sysMenu) {
        int res = sysMenuMapper.insert(sysMenu);
        if(res<=0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }
        SysMenu parentMenu=sysMenuMapper.selectById(sysMenu.getParentId());
        if(parentMenu!=null){
            sysRoleMenuMapper.setIsHalf(parentMenu.getId());
        }
    }

    @Override
    public void editSysMenu(SysMenu sysMenu) {
        int res = sysMenuMapper.updateById(sysMenu);
        if(res<0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteSysMenu(Long id) {
        Long count = sysMenuMapper.selectCount(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId , id));
        if(count>0){
            throw new MyException(ResultCodeEnum.NODE_ERROR);
        }else{
            sysMenuMapper.deleteById(id);
        }
    }

    @Override
    public Map<String, Object> findSysRoleMenuByRoleId(Long roleId) {
        Map<String,Object> map=new HashMap<>();
        map.put("sysMenuList",this.findNodes());

        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<SysRoleMenu>();
        wrapper.eq(SysRoleMenu::getRoleId , roleId);
        wrapper.eq(SysRoleMenu::getIsHalf,0);

        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(wrapper);
        List<Long> roleMenuIds=new ArrayList<>();
        for(SysRoleMenu item:sysRoleMenus){
            roleMenuIds.add(item.getMenuId());
        }

        map.put("roleMenuIds",roleMenuIds);
        return map;
    }

    @Override
    public List<SysMenuVo> findMenuByUserId() {
        SysUser sysUser = AuthContextUtil.get();
        Long userId = sysUser.getId();

        this.allSysMenu=sysMenuMapper.findMenuByUserId(userId);
        SysMenu rootNode=new SysMenu();
        rootNode.setId(0L);

        List<SysMenu> sysMenus = this.dfsFindNodes(rootNode);

        return buildMenus(sysMenus);
    }

    private List<SysMenuVo> buildMenus(List<SysMenu> menus) {

        List<SysMenuVo> sysMenuVoList = new LinkedList<SysMenuVo>();
        for (SysMenu sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<SysMenu> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }
}
