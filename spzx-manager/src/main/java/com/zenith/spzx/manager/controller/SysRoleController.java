package com.zenith.spzx.manager.controller;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.manager.service.SysRoleService;
import com.zenith.spzx.model.dto.system.SysRoleDto;
import com.zenith.spzx.model.entity.system.SysRole;
import com.zenith.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/system/sysRole")
@Tag(name="System Role Managing Api")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Operation(summary="Get role by role name with page limit")
    @PostMapping("/{current}/{limit}")
    public Result<PageInfo<SysRole>> findByPage(@PathVariable Integer current, @PathVariable Integer limit, @RequestBody SysRoleDto sysRoleDto){
        return Result.success(sysRoleService.queryRole(sysRoleDto,current,limit));
    }

    @Operation(summary = "Insert a new system role")
    @PostMapping
    public Result<Object> saveSysRole(@RequestBody SysRole sysRole){
        sysRoleService.saveSysRole(sysRole);
        return Result.success(null);
    }

    @Operation(summary = "Edit a system role by id")
    @PutMapping
    public Result<Object> editSysRole(@RequestBody SysRole sysRole){
        sysRoleService.editSysRole(sysRole);
        return Result.success(null);
    }

    @Operation(summary = "Delete a system role by id")
    @DeleteMapping("/{id}")
    public Result<Object> deleteSysRole(@PathVariable Long id){
        sysRoleService.deleteSysRole(id);
        return Result.success(null);
    }
}
