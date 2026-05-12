package com.sporty.jackpot.infrastructure.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class JackpotEntity {

    private String id;
    private BigDecimal initialPoolAmount;
    private BigDecimal currentPoolAmount;
    private String contributionStrategyType;
    private BigDecimal contributionRate;
    private BigDecimal contributionDecayRate;
    private String rewardStrategyType;
    private BigDecimal rewardChance;
    private BigDecimal rewardGrowthRate;
    private BigDecimal rewardPoolLimit;
    private Integer version;

    public JackpotEntity() {
    }

    public JackpotEntity(String id, BigDecimal initialPoolAmount, BigDecimal currentPoolAmount,
                         String contributionStrategyType, BigDecimal contributionRate,
                         BigDecimal contributionDecayRate, String rewardStrategyType,
                         BigDecimal rewardChance, BigDecimal rewardGrowthRate,
                         BigDecimal rewardPoolLimit, Integer version) {
        this.id = id;
        this.initialPoolAmount = initialPoolAmount;
        this.currentPoolAmount = currentPoolAmount;
        this.contributionStrategyType = contributionStrategyType;
        this.contributionRate = contributionRate;
        this.contributionDecayRate = contributionDecayRate;
        this.rewardStrategyType = rewardStrategyType;
        this.rewardChance = rewardChance;
        this.rewardGrowthRate = rewardGrowthRate;
        this.rewardPoolLimit = rewardPoolLimit;
        this.version = version;
    }
}
