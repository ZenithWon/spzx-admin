package com.zenith.spzx.common.log.annotation;

import com.zenith.spzx.common.log.enums.OperationType;
import com.zenith.spzx.common.log.enums.OperatorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    public String title();								// 模块名称
    public OperatorType operatorType() default OperatorType.MANAGE;	// 操作人类别
    public OperationType businessType() ;     // 业务类型（0其它 1新增 2修改 3删除）
}
