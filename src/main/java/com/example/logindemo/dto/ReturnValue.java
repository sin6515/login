package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.logindemo.dto.ConstantValue.*;

/**
 * @author hrh13
 * @date 2021/7/28
 */
@Data
@JSONType(orders = {"error","code", "message", "detail"})
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReturnValue<T> {
    private String error;
    private Integer code;
    private String message;
    private T detail;

    public static<T> ReturnValue<T> success(T detail) {
        ReturnValue returnValue = new ReturnValue();
        returnValue.setCode(OK_CODE);
        returnValue.setDetail(detail);
        return returnValue;
    }
    public static<T> ReturnValue<T> fail(Integer code,String message,T detail) {
        ReturnValue returnValue = new ReturnValue();
        returnValue.setCode(code);
        returnValue.setMessage(message);
        returnValue.setError(errorMap.get(code));
        returnValue.setDetail(detail);
        return returnValue;
    }

}
