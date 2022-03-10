package com.atcdi.digital.entity;

public class StandardException extends RuntimeException{
    public int code;
    public StandardException(int code, String message) {
        super(message);
        this.code = code;
    }
}
