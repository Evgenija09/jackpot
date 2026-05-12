package com.sporty.jackpot.exception;

public class ServiceUnavailableException extends JackpotServiceException {

    public ServiceUnavailableException(String message) {
        super(message, ErrorCode.SERVICE_UNAVAILABLE);
    }
}
