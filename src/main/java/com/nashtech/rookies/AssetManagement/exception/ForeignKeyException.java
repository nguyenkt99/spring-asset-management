package com.nashtech.rookies.AssetManagement.exception;

public class ForeignKeyException extends RuntimeException{
    public ForeignKeyException(String msg){
        super(msg);
    }
}
