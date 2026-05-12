package com.sporty.jackpot.infrastructure.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class JackpotContributionEntity {

    private Long id;
    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal stakeAmount;
    private BigDecimal contributionAmount;
    private BigDecimal currentJackpotAmount;
    private LocalDateTime createdAt;

    public JackpotContributionEntity() {
    }

    public JackpotContributionEntity(Long id, String betId, String userId, String jackpotId,
                                     BigDecimal stakeAmount, BigDecimal contributionAmount,
                                     BigDecimal currentJackpotAmount, LocalDateTime createdAt) {
        this.id = id;
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.stakeAmount = stakeAmount;
        this.contributionAmount = contributionAmount;
        this.currentJackpotAmount = currentJackpotAmount;
        this.createdAt = createdAt;
    }

}
