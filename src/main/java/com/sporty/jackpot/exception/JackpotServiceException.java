package com.sporty.jackpot.exception;

public class JackpotServiceException extends RuntimeException {

    private final ErrorCode errorCode;

    public JackpotServiceException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public JackpotServiceException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
