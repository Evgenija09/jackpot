package com.sporty.jackpot.infrastructure.persistence.repository;

import com.sporty.jackpot.domain.repository.JackpotRepository;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotEntity;
import com.sporty.jackpot.infrastructure.persistence.mapper.JackpotMyBatisMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class JackpotRepositoryImpl implements JackpotRepository {

    private final JackpotMyBatisMapper mapper;

    public JackpotRepositoryImpl(JackpotMyBatisMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<JackpotEntity> findById(String jackpotId) {
        return mapper.findById(jackpotId);
    }

    @Override
    public Optional<JackpotEntity> findByIdForUpdate(String jackpotId) {
        return mapper.findByIdForUpdate(jackpotId);
    }

    @Override
    public int updatePoolAmount(String jackpotId, BigDecimal newAmount, Integer expectedVersion) {
        return mapper.updatePoolAmount(jackpotId, newAmount, expectedVersion);
    }

    @Override
    public int resetPool(String jackpotId, Integer expectedVersion) {
        return mapper.resetPool(jackpotId, expectedVersion);
    }

    @Override
    public void save(JackpotEntity jackpot) {
        mapper.save(jackpot);
    }
}
