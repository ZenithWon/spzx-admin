package com.zenith.spzx.common.exception;

import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import lombok.Data;

@Data
public class MyException extends RuntimeException{
    private Integer code;
    private String msg;
    private ResultCodeEnum resultCodeEnum;

    public MyException(ResultCodeEnum resultCodeEnum){
        this.code=resultCodeEnum.getCode();
        this.msg=resultCodeEnum.getMessage();
        this.resultCodeEnum=resultCodeEnum;
    }

}
