package com.kakaogroom6.server.errors.code;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getHttpStatus();
    Integer getCode();
    String getMessage();
}
