package com.sporty.jackpot.infrastructure.persistence.repository;

import com.sporty.jackpot.domain.model.JackpotReward;
import com.sporty.jackpot.domain.repository.JackpotRewardRepository;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotRewardEntity;
import com.sporty.jackpot.infrastructure.persistence.mapper.JackpotRewardDomainMapper;
import com.sporty.jackpot.infrastructure.persistence.mapper.JackpotRewardMyBatisMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JackpotRewardRepositoryImpl implements JackpotRewardRepository {

    private final JackpotRewardMyBatisMapper mapper;
    private final JackpotRewardDomainMapper rewardDomainMapper;

    public JackpotRewardRepositoryImpl(JackpotRewardMyBatisMapper mapper,
                                       JackpotRewardDomainMapper rewardDomainMapper) {
        this.mapper = mapper;
        this.rewardDomainMapper = rewardDomainMapper;
    }

    @Override
    public boolean existsByBetId(String betId) {
        return mapper.existsByBetId(betId);
    }

    @Override
    public void save(JackpotReward reward) {
        JackpotRewardEntity entity = rewardDomainMapper.toEntity(reward);
        mapper.save(entity);
    }
}
