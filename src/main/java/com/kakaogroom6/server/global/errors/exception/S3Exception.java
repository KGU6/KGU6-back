package com.kakaogroom6.server.global.errors.exception;

import com.kakaogroom6.server.global.errors.code.ErrorCode;

public class S3Exception extends RuntimeException {

    private final ErrorCode errorCode;

    public S3Exception(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
