package com.sycoldstorage.wms.application.exception;

/**
 * 데이터가 중복되는 경우 Exception
 */
public class DuplicatedDataException extends RuntimeException {
    public DuplicatedDataException() {
        super();
    }

    public DuplicatedDataException(String message) {
        super(message);
    }
}
