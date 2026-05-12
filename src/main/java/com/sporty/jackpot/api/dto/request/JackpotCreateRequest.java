package com.sporty.jackpot.api.dto.request;

import com.sporty.jackpot.domain.strategy.contribution.ContributionStrategyType;
import com.sporty.jackpot.domain.strategy.reward.RewardStrategyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class JackpotCreateRequest {

    @NotBlank
    private String jackpotId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal initialPoolAmount;

    @NotNull
    private ContributionStrategyType contributionStrategyType;

    @NotNull
    @DecimalMin("0.0001")
    private BigDecimal contributionRate;

    private BigDecimal contributionDecayRate;

    @NotNull
    private RewardStrategyType rewardStrategyType;

    @NotNull
    @DecimalMin("0.0001")
    private BigDecimal rewardChance;

    private BigDecimal rewardGrowthRate;

    private BigDecimal rewardPoolLimit;

    public JackpotCreateRequest() {
    }

    public JackpotCreateRequest(String jackpotId, BigDecimal initialPoolAmount,
                                ContributionStrategyType contributionStrategyType,
                                BigDecimal contributionRate, BigDecimal contributionDecayRate,
                                RewardStrategyType rewardStrategyType, BigDecimal rewardChance,
                                BigDecimal rewardGrowthRate, BigDecimal rewardPoolLimit) {
        this.jackpotId = jackpotId;
        this.initialPoolAmount = initialPoolAmount;
        this.contributionStrategyType = contributionStrategyType;
        this.contributionRate = contributionRate;
        this.contributionDecayRate = contributionDecayRate;
        this.rewardStrategyType = rewardStrategyType;
        this.rewardChance = rewardChance;
        this.rewardGrowthRate = rewardGrowthRate;
        this.rewardPoolLimit = rewardPoolLimit;
    }
}
