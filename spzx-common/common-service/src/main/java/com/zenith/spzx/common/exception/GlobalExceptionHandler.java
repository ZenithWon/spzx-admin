package com.zenith.spzx.common.exception;

import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<Object> exceptionHandler(Exception e){
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(MyException.class)
    public Result<Object>myExceptionHandler(MyException e){
        log.error(e.getMsg());
        return Result.build(null, e.getResultCodeEnum());
    }
}
