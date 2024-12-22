package com.hydroyura.prodms.archive.server.exception.model.db;

public class RatePatchException extends RuntimeException {

    public RatePatchException(String msg) {
        super(msg);
    }

    public RatePatchException(String msg, Throwable e) {
        super(msg, e);
    }

}
