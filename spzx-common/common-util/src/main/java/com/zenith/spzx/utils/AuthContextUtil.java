package com.zenith.spzx.utils;

import com.zenith.spzx.model.entity.system.SysUser;
import com.zenith.spzx.model.entity.user.UserInfo;

public class AuthContextUtil {
    public static final ThreadLocal<SysUser> threadLocal=new ThreadLocal<>();

    public static void set(SysUser sysUser){
        threadLocal.set(sysUser);
    }

    public static SysUser get(){
        return threadLocal.get();
    }

    public static  void remove(){
        threadLocal.remove();
    }

    private static final ThreadLocal<UserInfo> userInfoThreadLocal = new ThreadLocal<>() ;


    // 定义存储数据的静态方法
    public static void setUserInfo(UserInfo userInfo) {
        userInfoThreadLocal.set(userInfo);
    }

    // 定义获取数据的方法
    public static UserInfo getUserInfo() {
        return userInfoThreadLocal.get() ;
    }

    // 删除数据的方法
    public static void removeUserInfo() {
        userInfoThreadLocal.remove();
    }
}
