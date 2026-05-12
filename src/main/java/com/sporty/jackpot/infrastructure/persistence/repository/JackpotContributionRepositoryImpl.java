package com.sporty.jackpot.infrastructure.persistence.repository;

import com.sporty.jackpot.domain.model.JackpotContribution;
import com.sporty.jackpot.domain.repository.JackpotContributionRepository;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotContributionEntity;
import com.sporty.jackpot.infrastructure.persistence.mapper.JackpotContributionDomainMapper;
import com.sporty.jackpot.infrastructure.persistence.mapper.JackpotContributionMyBatisMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JackpotContributionRepositoryImpl implements JackpotContributionRepository {

    private final JackpotContributionMyBatisMapper mapper;
    private final JackpotContributionDomainMapper contributionDomainMapper;

    public JackpotContributionRepositoryImpl(JackpotContributionMyBatisMapper mapper,
                                             JackpotContributionDomainMapper contributionDomainMapper) {
        this.mapper = mapper;
        this.contributionDomainMapper = contributionDomainMapper;
    }

    @Override
    public boolean existsByBetId(String betId) {
        return mapper.existsByBetId(betId);
    }

    @Override
    public Optional<JackpotContributionEntity> findByBetId(String betId) {
        return mapper.findByBetId(betId);
    }

    @Override
    public void save(JackpotContribution contribution) {
        JackpotContributionEntity entity = contributionDomainMapper.toEntity(contribution);
        mapper.save(entity);
    }

    @Override
    public long countByBetId(String betId) {
        return mapper.countByBetId(betId);
    }
}
