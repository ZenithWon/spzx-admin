package com.zenith.spzx.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.manager.mapper.SysMenuMapper;
import com.zenith.spzx.manager.service.SysMenuService;
import com.zenith.spzx.model.entity.system.SysMenu;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;

    List<SysMenu> dfsFindNodes(SysMenu sysMenu){
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId , sysMenu.getId())
                .orderByAsc(SysMenu::getSortValue);

        List<SysMenu> children=sysMenuMapper.selectList(wrapper);
        if(children==null||children.size()==0){
            return null;
        }
        for(SysMenu child:children){
            child.setChildren(dfsFindNodes(child));
        }
        return children;
    }

    @Override
    public List<SysMenu> findNodes() {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId , 0)
                .orderByAsc(SysMenu::getSortValue);

        List<SysMenu> parentMenus=sysMenuMapper.selectList(wrapper);
        for(SysMenu sysMenu:parentMenus){
            List<SysMenu> children = dfsFindNodes(sysMenu);
            sysMenu.setChildren(children);
        }

        return parentMenus;
    }

    @Override
    public void saveSysMenu(SysMenu sysMenu) {
        int res = sysMenuMapper.insert(sysMenu);
        if(res<=0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
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
}
