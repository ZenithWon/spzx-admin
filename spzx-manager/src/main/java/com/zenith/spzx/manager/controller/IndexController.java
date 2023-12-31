package com.zenith.spzx.manager.controller;

import com.zenith.spzx.common.log.annotation.Log;
import com.zenith.spzx.common.log.enums.OperationType;
import com.zenith.spzx.manager.service.SysMenuService;
import com.zenith.spzx.manager.service.SysUserService;
import com.zenith.spzx.manager.service.ValidateCodeService;
import com.zenith.spzx.model.dto.system.LoginDto;
import com.zenith.spzx.model.entity.system.SysUser;
import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import com.zenith.spzx.model.vo.system.LoginVo;
import com.zenith.spzx.model.vo.system.SysMenuVo;
import com.zenith.spzx.model.vo.system.ValidateCodeVo;
import com.zenith.spzx.utils.AuthContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "System Authorize Api")
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ValidateCodeService validateCodeService;
    @Autowired
    private SysMenuService sysMenuService;

    @Operation(summary = "System user login")
    @PostMapping(value = "/login")
    @Log(title = "auth:login",businessType = OperationType.AUTH)
    public Result<LoginVo> sysUserLogin(@RequestBody LoginDto dto){
        LoginVo loginVo = sysUserService.sysUserLogin(dto);
        return Result.success(loginVo);
    }

    @Operation(summary = "System user logout")
    @GetMapping(value = "/logout")
    @Log(title = "auth:logout",businessType = OperationType.AUTH)
    public Result<Object> sysUserLogout(@RequestHeader(name = "token") String token){
        Boolean res= sysUserService.sysUserLogout(token);
        if(res){
            return Result.success(null);
        }else {
            return Result.build(null,ResultCodeEnum.LOGIN_AUTH);
        }

    }

    @Operation(summary = "Generate Validate Code")
    @GetMapping(value = "/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode(){
        ValidateCodeVo validateCodeVo=validateCodeService.generateValidataCode();
        return Result.success(validateCodeVo);
    }

    @GetMapping("/getUserInfo")
    @Operation(summary = "Get system user information")
    public Result<SysUser> getUserInfo(){
        return Result.success(AuthContextUtil.get());
    }

    @GetMapping("/menus")
    @Operation(summary = "Get user's menus")
    public Result<List<SysMenuVo>> menus(){
        List<SysMenuVo> sysMenuVoList = sysMenuService.findMenuByUserId();
        return Result.success(sysMenuVoList);
    }

}
