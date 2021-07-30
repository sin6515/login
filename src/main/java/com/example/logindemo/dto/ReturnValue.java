package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hrh13
 * @date 2021/7/28
 */
@Data
@JSONType(orders = {"state", "error","code", "message", "detail"})
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReturnValue<T> {
    private String state;
    private String error;
    private Integer code;
    private String message;
    private T detail;

    public static<T> ReturnValue<T> success(T detail) {
        ReturnValue returnValue = new ReturnValue();
        returnValue.setCode(200);
        returnValue.setDetail(detail);
        return returnValue;
    }
    public ReturnValue(String state,String error,Integer code, String message, ReturnDetailValue returnDetailValue) {
        setState(state);
        setError(error);
        setCode(code);
        setMessage(message);
        setDetail(returnDetailValue);
    }

}
