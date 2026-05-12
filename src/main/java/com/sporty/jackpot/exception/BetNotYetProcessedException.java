package com.sporty.jackpot.exception;

public class BetNotYetProcessedException extends JackpotServiceException {

    public BetNotYetProcessedException(String betId) {
        super("Bet not yet processed: " + betId, ErrorCode.BET_NOT_YET_PROCESSED);
    }
}
