package com.sporty.jackpot.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Bet {

    private final String betId;
    private final String userId;
    private final String jackpotId;
    private final Money amount;

    @JsonCreator
    public Bet(
            @JsonProperty("betId") String betId,
            @JsonProperty("userId") String userId,
            @JsonProperty("jackpotId") String jackpotId,
            @JsonProperty("amount") Money amount) {
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.amount = amount;
    }
}
