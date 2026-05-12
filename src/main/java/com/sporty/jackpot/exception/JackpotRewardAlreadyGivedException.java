package com.sporty.jackpot.exception;

public class JackpotRewardAlreadyGivedException extends JackpotServiceException {

    public JackpotRewardAlreadyGivedException(String betId) {
        super("Reward already given for bet: " + betId, ErrorCode.REWARD_ALREADY_GIVEN);
    }
}
