package com.sporty.jackpot.domain.model;

import com.sporty.jackpot.domain.strategy.contribution.ContributionStrategyType;
import com.sporty.jackpot.domain.strategy.reward.RewardStrategyType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Jackpot {

    private String id;
    private BigDecimal initialPoolAmount;
    private BigDecimal currentPoolAmount;
    private ContributionStrategyType contributionStrategyType;
    private BigDecimal contributionRate;
    private BigDecimal contributionDecayRate;
    private RewardStrategyType rewardStrategyType;
    private BigDecimal rewardChance;
    private BigDecimal rewardGrowthRate;
    private BigDecimal rewardPoolLimit;
    private Integer version;

    public Jackpot() {
    }

    public Jackpot(String id, BigDecimal initialPoolAmount, BigDecimal currentPoolAmount,
                   ContributionStrategyType contributionStrategyType, BigDecimal contributionRate,
                   BigDecimal contributionDecayRate, RewardStrategyType rewardStrategyType,
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

    public void applyContribution(BigDecimal contributionAmount) {
        this.currentPoolAmount = this.currentPoolAmount.add(contributionAmount);
    }

    public void reset() {
        this.currentPoolAmount = this.initialPoolAmount;
    }

    public boolean isAtPoolLimit() {
        if (rewardPoolLimit == null) {
            return false;
        }
        return currentPoolAmount.compareTo(rewardPoolLimit) >= 0;
    }
}
