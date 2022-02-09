package com.sycoldstorage.wms.application.exception;

/**
 * FK Constraint 관려 Exception
 * 예 : 삭제시 자식 row가 존재하는 경우 삭제 불가할 때
 */
public class ForeignKeyConstraintException extends RuntimeException {
    public ForeignKeyConstraintException() {
        super();
    }

    public ForeignKeyConstraintException(String message) {
        super(message);
    }
}
