package com.sporty.jackpot.domain.strategy.contribution;

import com.sporty.jackpot.domain.model.Jackpot;
import org.springframework.stereotype.Component;

@Component
public class ContributionStrategyFactory {

    public ContributionStrategy resolve(Jackpot jackpot) {
        return switch (jackpot.getContributionStrategyType()) {
            case FIXED -> new FixedContributionStrategy(jackpot.getContributionRate());
            case VARIABLE -> new VariableContributionStrategy(
                    jackpot.getContributionRate(),
                    jackpot.getContributionDecayRate()
            );
            default -> throw new IllegalArgumentException(
                    "Unknown contribution strategy type: " + jackpot.getContributionStrategyType()
            );
        };
    }
}
