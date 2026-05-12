package com.sporty.jackpot.infrastructure.persistence.mapper;

import com.sporty.jackpot.infrastructure.persistence.entity.JackpotRewardEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JackpotRewardMyBatisMapper {

    boolean existsByBetId(String betId);

    void save(JackpotRewardEntity reward);
}
