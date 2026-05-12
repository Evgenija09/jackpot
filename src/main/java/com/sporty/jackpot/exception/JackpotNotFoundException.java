package com.sporty.jackpot.exception;

public class JackpotNotFoundException extends JackpotServiceException {

    public JackpotNotFoundException(String jackpotId) {
        super("Jackpot not found: " + jackpotId, ErrorCode.JACKPOT_NOT_FOUND);
    }
}
