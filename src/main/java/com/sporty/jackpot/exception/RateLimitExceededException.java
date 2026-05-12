package com.sporty.jackpot.exception;

public class RateLimitExceededException extends JackpotServiceException {

    public RateLimitExceededException(String message) {
        super(message, ErrorCode.RATE_LIMIT_EXCEEDED);
    }
}
