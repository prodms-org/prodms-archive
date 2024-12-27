package com.hydroyura.prodms.archive.server.exception.model.db;

public class RateNotExistException extends RuntimeException {

    public RateNotExistException(String msg) {
        super(msg);
    }

    public RateNotExistException(String msg, Throwable e) {
        super(msg, e);
    }

}
