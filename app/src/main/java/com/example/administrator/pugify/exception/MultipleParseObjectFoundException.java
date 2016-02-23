package com.example.administrator.pugify.exception;

public class MultipleParseObjectFoundException extends Exception{

    public MultipleParseObjectFoundException(){}

    public MultipleParseObjectFoundException(String message){
        super(message);
    }

    public MultipleParseObjectFoundException(String message, Throwable throwable) { super(message,throwable); }
}
