package com.zenith.spzx.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zenith.spzx.model.entity.base.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_user_role")
public class SysUserRole extends BaseEntity {

    private Long roleId;       // 角色id
    private Long userId;       // 用户id

}
