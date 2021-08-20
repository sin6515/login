package com.example.logindemo.error;

/**
 * @author hrh13
 * @date 2021/8/19
 */
public class PasswordErrorException extends Exception {
    private String message;

    public PasswordErrorException(String message) {
        super(message);
        this.message = message;
    }
}