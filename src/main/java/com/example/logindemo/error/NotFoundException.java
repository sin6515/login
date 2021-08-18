package com.example.logindemo.error;

/**
 * @author hrh13
 * @date 2021/8/18
 */
public class NotFoundException extends Exception{
    private String message;
    public NotFoundException(String message){
        super(message);
        this.message=message;
    }
}
