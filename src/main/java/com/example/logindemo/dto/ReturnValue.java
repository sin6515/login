package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.logindemo.dto.ErrorConstantValue.ERROR_MAP;
import static com.example.logindemo.dto.ErrorConstantValue.OK_CODE;

/**
 * @author hrh13
 * @date 2021/7/28
 */
@Data
@JSONType(orders = {"error", "code", "message", "detail"})
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReturnValue<T> {
    private String error;
    private Integer code;
    private String message;
    private T detail;

    public static <T> ReturnValue<T> success(T detail) {
        ReturnValue returnValue = new ReturnValue();
        returnValue.setCode(OK_CODE);
        returnValue.setDetail(detail);
        return returnValue;
    }

    public static ReturnValue fail(Integer code, String message, String detail) {
        ReturnValue returnValue = new ReturnValue();
        returnValue.setCode(code);
        returnValue.setMessage(message);
        returnValue.setError(ERROR_MAP.get(code));
        returnValue.setDetail(detail);
        return returnValue;
    }
}
