package com.sporty.jackpot.infrastructure.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class JackpotRewardEntity {

    private Long id;
    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal rewardAmount;
    private LocalDateTime createdAt;

    public JackpotRewardEntity() {
    }

    public JackpotRewardEntity(Long id, String betId, String userId, String jackpotId,
                               BigDecimal rewardAmount, LocalDateTime createdAt) {
        this.id = id;
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.rewardAmount = rewardAmount;
        this.createdAt = createdAt;
    }

}
