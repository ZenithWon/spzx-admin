package com.zenith.spzx.manager.controller;

import com.zenith.spzx.common.log.annotation.Log;
import com.zenith.spzx.common.log.enums.OperationType;
import com.zenith.spzx.manager.service.SysMenuService;
import com.zenith.spzx.model.entity.system.SysMenu;
import com.zenith.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/system/sysMenu")
@Tag(name = "System Menu Managing Api")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("/findNodes")
    @Operation(summary = "Find menu nodes with tree struct")
    @Log(title = "sysMenu:list",businessType = OperationType.OTHER)
    public Result findNodes(){
        return Result.success(sysMenuService.findNodes());
    }

    @PostMapping
    @Operation(summary = "Insert a new system menu")
    @Log(title = "sysMenu:insert",businessType = OperationType.INSERT)
    public Result saveMenuNodes(@RequestBody SysMenu sysMenu){
        sysMenuService.saveSysMenu(sysMenu);
        return Result.success(null);
    }

    @PutMapping
    @Operation(summary = "Edit a system menu")
    @Log(title = "sysMenu:edit",businessType = OperationType.UPDATE)
    public Result editMenuNodes(@RequestBody SysMenu sysMenu){
        sysMenuService.editSysMenu(sysMenu);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a system menu by id")
    @Log(title = "sysMenu:delete",businessType = OperationType.DELETE)
    public Result editMenuNodes(@PathVariable Long id){
        sysMenuService.deleteSysMenu(id);
        return Result.success(null);
    }

    @GetMapping("/findRoleMenu/{roleId}")
    @Operation(summary = "Get role's menu")
    @Log(title = "sysMenu:list",businessType = OperationType.OTHER)
    public Result<Object> findSysRoleMenuByRoleId(@PathVariable Long roleId){
        Map<String,Object> map=sysMenuService.findSysRoleMenuByRoleId(roleId);
        return Result.success(map);
    }
}
