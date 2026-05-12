package com.sporty.jackpot.domain.service;

import com.sporty.jackpot.domain.model.Bet;
import com.sporty.jackpot.domain.model.Jackpot;
import com.sporty.jackpot.domain.model.JackpotContribution;
import com.sporty.jackpot.domain.strategy.contribution.ContributionStrategy;
import com.sporty.jackpot.domain.strategy.contribution.ContributionStrategyFactory;
import com.sporty.jackpot.exception.JackpotNotFoundException;
import com.sporty.jackpot.exception.OptimisticLockException;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotEntity;
import com.sporty.jackpot.infrastructure.persistence.mapper.JackpotDomainMapper;
import com.sporty.jackpot.domain.repository.JackpotContributionRepository;
import com.sporty.jackpot.domain.repository.JackpotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class JackpotContributionService {

    private static final Logger log = LoggerFactory.getLogger(JackpotContributionService.class);

    private final JackpotRepository jackpotRepository;
    private final JackpotContributionRepository contributionRepository;
    private final ContributionStrategyFactory contributionStrategyFactory;
    private final JackpotDomainMapper jackpotDomainMapper;

    public JackpotContributionService(JackpotRepository jackpotRepository,
                                      JackpotContributionRepository contributionRepository,
                                      ContributionStrategyFactory contributionStrategyFactory,
                                      JackpotDomainMapper jackpotDomainMapper) {
        this.jackpotRepository = jackpotRepository;
        this.contributionRepository = contributionRepository;
        this.contributionStrategyFactory = contributionStrategyFactory;
        this.jackpotDomainMapper = jackpotDomainMapper;
    }

    @Transactional
    public void processContribution(Bet bet) {
        if (contributionRepository.existsByBetId(bet.getBetId())) {
            log.info("Duplicate contribution ignored for betId={}", bet.getBetId());
            return;
        }

        JackpotEntity entity = jackpotRepository.findById(bet.getJackpotId())
                .orElseThrow(() -> new JackpotNotFoundException(bet.getJackpotId()));

        Jackpot jackpot = jackpotDomainMapper.toDomain(entity);
        ContributionStrategy strategy = contributionStrategyFactory.resolve(jackpot);

        BigDecimal contributionAmount = strategy.calculate(
                bet.getAmount().amount(),
                jackpot.getCurrentPoolAmount()
        );

        jackpot.applyContribution(contributionAmount);

        int rowsUpdated = jackpotRepository.updatePoolAmount(
                jackpot.getId(), jackpot.getCurrentPoolAmount(), jackpot.getVersion()
        );
        if (rowsUpdated == 0) {
            throw new OptimisticLockException(jackpot.getId());
        }

        JackpotContribution contribution = new JackpotContribution();
        contribution.setBetId(bet.getBetId());
        contribution.setUserId(bet.getUserId());
        contribution.setJackpotId(bet.getJackpotId());
        contribution.setStakeAmount(bet.getAmount().amount());
        contribution.setContributionAmount(contributionAmount);
        contribution.setCurrentJackpotAmount(jackpot.getCurrentPoolAmount());
        contribution.setCreatedAt(LocalDateTime.now());

        contributionRepository.save(contribution);
    }
}
