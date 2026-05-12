package com.sporty.jackpot.domain.repository;

import com.sporty.jackpot.domain.model.JackpotReward;

public interface JackpotRewardRepository {
    boolean existsByBetId(String betId);
    void save(JackpotReward reward);
}
