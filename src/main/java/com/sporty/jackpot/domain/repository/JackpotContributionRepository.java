package com.sporty.jackpot.domain.repository;

import com.sporty.jackpot.domain.model.JackpotContribution;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotContributionEntity;
import java.util.Optional;

public interface JackpotContributionRepository {
    boolean existsByBetId(String betId);
    Optional<JackpotContributionEntity> findByBetId(String betId);
    void save(JackpotContribution contribution);
    long countByBetId(String betId);
}
