package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

/**
 * @author hrh13
 * @date 2021/7/28
 */
@Data
@JSONType(orders = {"state", "error","code", "message", "detail"})
public class ReturnValue {
    private String state;
    private String error;
    private Integer code;
    private String message;
    private ReturnDetailValue detail;

    public ReturnValue(String state,Integer code, String message, ReturnDetailValue returnDetailValue) {
        setState(state);
        setCode(code);
        setMessage(message);
        setDetail(returnDetailValue);
    }
    public ReturnValue(String state,String error,Integer code, String message, ReturnDetailValue returnDetailValue) {
        setState(state);
        setError(error);
        setCode(code);
        setMessage(message);
        setDetail(returnDetailValue);
    }

}
