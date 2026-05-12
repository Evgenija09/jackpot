package com.sporty.jackpot.domain.repository;

import com.sporty.jackpot.infrastructure.persistence.entity.JackpotEntity;
import java.math.BigDecimal;
import java.util.Optional;

public interface JackpotRepository {
    Optional<JackpotEntity> findById(String jackpotId);
    Optional<JackpotEntity> findByIdForUpdate(String jackpotId);
    int updatePoolAmount(String jackpotId, BigDecimal newAmount, Integer expectedVersion);
    int resetPool(String jackpotId, Integer expectedVersion);
    void save(JackpotEntity jackpot);
}
