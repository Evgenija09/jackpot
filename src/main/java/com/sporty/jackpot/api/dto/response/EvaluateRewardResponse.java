package com.sporty.jackpot.api.dto.response;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class EvaluateRewardResponse {

    private String betId;
    private String jackpotId;
    private boolean won;
    private BigDecimal rewardAmount;
    private String message;

    public EvaluateRewardResponse() {
    }

    public EvaluateRewardResponse(String betId, String jackpotId, boolean won,
                                   BigDecimal rewardAmount, String message) {
        this.betId = betId;
        this.jackpotId = jackpotId;
        this.won = won;
        this.rewardAmount = rewardAmount;
        this.message = message;
    }

}
