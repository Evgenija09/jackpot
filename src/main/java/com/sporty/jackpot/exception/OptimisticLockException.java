package com.sporty.jackpot.exception;

public class OptimisticLockException extends JackpotServiceException {

    public OptimisticLockException(String jackpotId) {
        super("Concurrent update detected for jackpot: " + jackpotId, ErrorCode.INTERNAL_ERROR);
    }
}
