package com.sporty.jackpot.domain.strategy.reward;

import com.sporty.jackpot.domain.model.Jackpot;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RewardStrategyFactory {

    public RewardStrategy resolve(Jackpot jackpot) {
        return switch (jackpot.getRewardStrategyType()) {
            case FIXED -> new FixedRewardStrategy(jackpot.getRewardChance(), new Random());
            case VARIABLE -> new VariableRewardStrategy(
                    jackpot.getRewardChance(),
                    jackpot.getRewardGrowthRate(),
                    jackpot.getRewardPoolLimit(),
                    new Random()
            );
            default -> throw new IllegalArgumentException(
                    "Unknown reward strategy type: " + jackpot.getRewardStrategyType()
            );
        };
    }
}
