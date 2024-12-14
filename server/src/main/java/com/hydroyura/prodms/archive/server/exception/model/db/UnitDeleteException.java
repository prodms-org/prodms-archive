package com.hydroyura.prodms.archive.server.exception.model.db;

public class UnitDeleteException extends RuntimeException {

    public UnitDeleteException(String msg) {
        super(msg);
    }

    public UnitDeleteException(String msg, Throwable e) {
        super(msg, e);
    }

}
