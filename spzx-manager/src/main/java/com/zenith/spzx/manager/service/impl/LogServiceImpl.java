package com.zenith.spzx.manager.service.impl;

import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.common.log.service.LogService;
import com.zenith.spzx.manager.mapper.SysOperLogMapper;
import com.zenith.spzx.model.entity.system.SysOperLog;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogServiceImpl implements LogService {
    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    public void saveSysOperLog(SysOperLog sysOperLog){
        int res = sysOperLogMapper.insert(sysOperLog);
        if(res<=0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }
    }

}
