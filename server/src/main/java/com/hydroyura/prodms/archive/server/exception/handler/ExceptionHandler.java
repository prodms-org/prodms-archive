package com.hydroyura.prodms.archive.server.exception.handler;

public interface ExceptionHandler<T extends Exception> {

    T getExceptionType();

    void handle(T e);

}
