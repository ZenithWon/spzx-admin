package com.zenith.spzx.manager.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zenith.spzx.model.entity.system.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    void deleteByRoleId(Long roleId);

    void setIsHalf(Long menuId);
}
