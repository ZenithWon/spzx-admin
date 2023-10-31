package com.zenith.spzx.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.common.log.annotation.Log;
import com.zenith.spzx.common.log.enums.OperationType;
import com.zenith.spzx.manager.mapper.SysRoleMapper;
import com.zenith.spzx.manager.mapper.SysRoleMenuMapper;
import com.zenith.spzx.manager.mapper.SysUserRoleMapper;
import com.zenith.spzx.manager.service.SysRoleService;
import com.zenith.spzx.model.dto.system.AssignMenuDto;
import com.zenith.spzx.model.dto.system.SysRoleDto;
import com.zenith.spzx.model.entity.system.SysRole;
import com.zenith.spzx.model.entity.system.SysRoleMenu;
import com.zenith.spzx.model.entity.system.SysUserRole;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public PageInfo<SysRole> queryRole(SysRoleDto sysRoleDto , Integer current , Integer limit) {
        PageHelper.startPage(current,limit);
        LambdaQueryWrapper<SysRole> wrapper=new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(sysRoleDto.getRoleName()),SysRole::getRoleName,sysRoleDto.getRoleName());
        wrapper.orderByAsc(SysRole::getCreateTime);
        List<SysRole> sysRoles = sysRoleMapper.selectList(wrapper);

        return new PageInfo<SysRole>(sysRoles);
    }

    @Override
    public void saveSysRole(SysRole sysRole) {
        int res= sysRoleMapper.insert(sysRole);
        if(res<=0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public void editSysRole(SysRole sysRole) {
        sysRole.setUpdateTime(new Date());
        log.debug(sysRole.getUpdateTime().toString());
        int res=sysRoleMapper.updateById(sysRole);
        if(res<=0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteSysRole(Long id) {
        Long count1 = sysRoleMenuMapper.selectCount(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId , id));
        if(count1>0){
            throw new MyException(ResultCodeEnum.DATABASE_DELETE_ERROR);
        }

        Long count2 = sysUserRoleMapper.selectCount(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId , id));
        if(count2>0){
            throw new MyException(ResultCodeEnum.DATABASE_DELETE_ERROR);
        }

        sysRoleMapper.deleteById(id);
    }

    @Override
    public Map<String,Object> findAll(Long userId) {
        Map<String,Object> map=new HashMap<>();
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        List<SysUserRole> assignedRoles=sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId,userId)
        );

        List<Long> sysUserRoles=new ArrayList<>();
        for(SysUserRole sysUserRole:assignedRoles){
            sysUserRoles.add(sysUserRole.getRoleId());
        }

        map.put("allRolesList",sysRoles);
        map.put("sysUserRoles",sysUserRoles);

        return map;
    }

    @Override
    @Transactional
    public void assignMenu(AssignMenuDto dto) {
        Long roleId= dto.getRoleId();
        List<Map<String, Number>> menuIdList = dto.getMenuIdList();

        sysRoleMenuMapper.deleteByRoleId(roleId);
        if(menuIdList==null||menuIdList.size()==0){
            return;
        }

        for(Map<String,Number> item:menuIdList){
            log.debug(item.toString());
            SysRoleMenu sysRoleMenu=new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(item.get("menuId").longValue());
            sysRoleMenu.setIsHalf(item.get("isHalf").intValue());

            int res=sysRoleMenuMapper.insert(sysRoleMenu);
            if(res<=0){
                throw new MyException(ResultCodeEnum.DATABASE_ERROR);
            }
        }

    }
}
