package com.kakaogroom6.server.global.errors.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, 400, "Invalid parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Internal server error"),

    //s3
    EMPTY_FILE_EXCEPTION(HttpStatus.BAD_REQUEST, 400, "The file is empty or the filename is null."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, 500, "An I/O error occurred while uploading the image to S3."),
    NO_FILE_EXTENTION(HttpStatus.BAD_REQUEST, 400, "The file does not have an extension."),
    INVALID_FILE_EXTENTION(HttpStatus.BAD_REQUEST, 400, "The file extension is invalid. Only jpg, jpeg, png, gif are allowed."),
    PUT_OBJECT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Failed to upload the image to S3."),
    IO_EXCEPTION_ON_IMAGE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, 500, "An I/O error occurred while deleting the image from S3."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "Member not found")
    ;

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;
}
