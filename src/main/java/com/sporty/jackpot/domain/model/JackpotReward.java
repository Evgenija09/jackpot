package com.sporty.jackpot.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class JackpotReward {

    private Long id;
    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal rewardAmount;
    private LocalDateTime createdAt;

    public JackpotReward() {
    }

    public JackpotReward(Long id, String betId, String userId, String jackpotId,
                         BigDecimal rewardAmount, LocalDateTime createdAt) {
        this.id = id;
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.rewardAmount = rewardAmount;
        this.createdAt = createdAt;
    }

}
