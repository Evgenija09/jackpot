package com.sporty.jackpot.infrastructure.persistence.mapper;

import com.sporty.jackpot.infrastructure.persistence.entity.JackpotContributionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface JackpotContributionMyBatisMapper {

    boolean existsByBetId(String betId);

    Optional<JackpotContributionEntity> findByBetId(String betId);

    void save(JackpotContributionEntity contribution);

    long countByBetId(String betId);
}
