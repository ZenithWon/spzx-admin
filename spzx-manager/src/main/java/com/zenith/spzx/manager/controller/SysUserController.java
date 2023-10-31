package com.zenith.spzx.manager.controller;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.common.log.annotation.Log;
import com.zenith.spzx.common.log.enums.OperationType;
import com.zenith.spzx.manager.service.SysUserService;
import com.zenith.spzx.model.dto.system.AssignRoleDto;
import com.zenith.spzx.model.dto.system.SysUserDto;
import com.zenith.spzx.model.entity.system.SysUser;
import com.zenith.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/system/sysUser")
@Tag(name = "System User Managing Api")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/{pageNum}/{pageSize}")
    @Operation(summary = "Query system users")
    @Log(title = "sysUser:list",businessType = OperationType.OTHER)
    public Result<PageInfo<SysUser>> pageQuerySysUser(SysUserDto dto, @PathVariable Integer pageNum,@PathVariable Integer pageSize){
        PageInfo<SysUser> pageInfo= sysUserService.pageQuerySysUser(pageNum,pageSize,dto);
        return Result.success(pageInfo);
    }

    @PostMapping
    @Operation(summary = "Insert a new system user")
    @Log(title = "sysUser:insert",businessType = OperationType.INSERT)
    public Result<Object> saveSysUser(@RequestBody SysUser sysUser){
        sysUserService.saveSysUser(sysUser);
        return Result.success(null);
    }

    @PutMapping
    @Operation(summary = "Edit system user")
    @Log(title = "sysUser:edit",businessType = OperationType.UPDATE)
    public Result<Object> editSysUser(@RequestBody SysUser sysUser){
        sysUserService.editSysUser(sysUser);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete system user")
    @Log(title = "sysUser:edit",businessType = OperationType.UPDATE)
    public Result<Object> deleteSysUser(@PathVariable Long id){
        sysUserService.deleteSysUserById(id);
        return Result.success(null);
    }

    @PostMapping("/assignRole")
    @Operation(summary = "Assign roles for user")
    @Log(title = "sysUser:edit",businessType = OperationType.UPDATE)
    public Result<Object> assignRole(@RequestBody AssignRoleDto dto){
        sysUserService.assignRole(dto);
        return Result.success(null);
    }
}
