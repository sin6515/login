package com.example.logindemo.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hrh13
 * @date 2021/8/17
 */
@ControllerAdvice
@RestController
public class ExceptionController{
//    public ExceptionController() {
//        super(new DefaultErrorAttributes(), new ErrorProperties());
//    }
//
//    @Override
//    @RequestMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
//        HttpStatus status = getStatus(request);
//        return new ResponseEntity(ReturnValue.fail(BAD_REQUEST_CODE, ERROR_TOKEN, request.getHeaders(HEADER_TOKEN)), status);
//    }
}
