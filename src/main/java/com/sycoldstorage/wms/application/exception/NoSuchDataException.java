package com.sycoldstorage.wms.application.exception;

/**
 * 데이터가 존재하지 않는 경우 Exception
 */
public class NoSuchDataException extends RuntimeException {
    public NoSuchDataException() {
        super();
    }

    public NoSuchDataException(String message) {
        super(message);
    }
}
