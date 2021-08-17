package com.example.logindemo.controller;

import com.example.logindemo.dto.ReturnValue;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.example.logindemo.dto.ConstantValue.HEADER_TOKEN;
import static com.example.logindemo.dto.ErrorConstantValue.BAD_REQUEST_CODE;
import static com.example.logindemo.dto.ErrorConstantValue.ERROR_TOKEN;

/**
 * @author hrh13
 * @date 2021/8/17
 */
@ControllerAdvice
@RestController
public class ExceptionController extends BasicErrorController {
    public ExceptionController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @Override
    @RequestMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        return new ResponseEntity(ReturnValue.fail(BAD_REQUEST_CODE, ERROR_TOKEN, request.getHeaders(HEADER_TOKEN)), status);
    }
}
