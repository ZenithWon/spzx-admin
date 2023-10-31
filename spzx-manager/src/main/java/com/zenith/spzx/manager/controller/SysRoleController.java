package com.zenith.spzx.manager.controller;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.common.log.annotation.Log;
import com.zenith.spzx.common.log.enums.OperationType;
import com.zenith.spzx.manager.service.SysRoleService;
import com.zenith.spzx.model.dto.system.AssignMenuDto;
import com.zenith.spzx.model.dto.system.SysRoleDto;
import com.zenith.spzx.model.entity.system.SysRole;
import com.zenith.spzx.model.entity.system.SysRoleMenu;
import com.zenith.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin/system/sysRole")
@Tag(name="System Role Managing Api")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Operation(summary="Get role by role name with page limit")
    @PostMapping("/{current}/{limit}")
    @Log(title = "sysRole:list",businessType = OperationType.OTHER)
    public Result<PageInfo<SysRole>> findByPage(@PathVariable Integer current, @PathVariable Integer limit, @RequestBody SysRoleDto sysRoleDto){
        return Result.success(sysRoleService.queryRole(sysRoleDto,current,limit));
    }

    @Operation(summary = "Insert a new system role")
    @PostMapping
    @Log(title = "sysRole:insert",businessType = OperationType.INSERT)
    public Result<Object> saveSysRole(@RequestBody SysRole sysRole){
        sysRoleService.saveSysRole(sysRole);
        return Result.success(null);
    }

    @Operation(summary = "Edit a system role by id")
    @PutMapping
    @Log(title = "sysRole:edit",businessType = OperationType.UPDATE)
    public Result<Object> editSysRole(@RequestBody SysRole sysRole){
        sysRoleService.editSysRole(sysRole);
        return Result.success(null);
    }

    @Operation(summary = "Delete a system role by id")
    @DeleteMapping("/{id}")
    @Log(title = "sysRole:delete",businessType = OperationType.DELETE)
    public Result<Object> deleteSysRole(@PathVariable Long id){
        sysRoleService.deleteSysRole(id);
        return Result.success(null);
    }

    @GetMapping("/findAllRoles/{userId}")
    @Log(title = "sysRole:list",businessType = OperationType.OTHER)
    @Operation(summary = "Get all roles for system user")
    public Result<Object> findAll(@PathVariable Long userId){
        Map<String,Object> map=sysRoleService.findAll(userId);
        return Result.success(map);
    }

    @PostMapping("/assignMenu")
    @Operation(summary = "Assign menu for role")
    @Log(title = "sysRole:update",businessType = OperationType.UPDATE)
    public Result<Object> assignMenu(@RequestBody AssignMenuDto dto){
        sysRoleService.assignMenu(dto);
        return Result.success(null);
    }


}
