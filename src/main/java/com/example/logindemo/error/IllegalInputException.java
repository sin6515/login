package com.example.logindemo.error;

/**
 * @author hrh13
 * @date 2021/8/19
 */
public class IllegalInputException extends Exception{
    private String message;
    public IllegalInputException(String message){
        super(message);
        this.message=message;
    }
}
