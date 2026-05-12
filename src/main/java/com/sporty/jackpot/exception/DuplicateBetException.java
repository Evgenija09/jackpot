package com.sporty.jackpot.exception;

public class DuplicateBetException extends JackpotServiceException {

    public DuplicateBetException(String betId) {
        super("Duplicate bet detected: " + betId, ErrorCode.DUPLICATE_BET);
    }
}
