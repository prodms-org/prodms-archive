package com.hydroyura.prodms.archive.server.exception.model.db;

public class UnitPatchException extends RuntimeException {

    public UnitPatchException(String msg) {
        super(msg);
    }

    public UnitPatchException(String msg, Throwable e) {
        super(msg, e);
    }

}
