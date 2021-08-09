package com.baratov.spring.springboot.myExcetion;

public class SaveObjectException extends Exception{

    public SaveObjectException() {
    }

    public SaveObjectException(String message) {
        super(message);
    }

    public SaveObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveObjectException(Throwable cause) {
        super(cause);
    }

    public SaveObjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
