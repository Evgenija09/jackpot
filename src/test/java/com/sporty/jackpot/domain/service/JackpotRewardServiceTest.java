package com.sporty.jackpot.domain.service;

import com.sporty.jackpot.api.dto.response.EvaluateRewardResponse;
import com.sporty.jackpot.domain.model.Jackpot;
import com.sporty.jackpot.domain.model.JackpotReward;
import com.sporty.jackpot.domain.strategy.contribution.ContributionStrategyType;
import com.sporty.jackpot.domain.strategy.reward.RewardStrategy;
import com.sporty.jackpot.domain.strategy.reward.RewardStrategyFactory;
import com.sporty.jackpot.domain.strategy.reward.RewardStrategyType;
import com.sporty.jackpot.exception.BetNotYetProcessedException;
import com.sporty.jackpot.exception.JackpotRewardAlreadyGivedException;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotContributionEntity;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotEntity;
import com.sporty.jackpot.infrastructure.persistence.mapper.JackpotDomainMapper;
import com.sporty.jackpot.domain.repository.JackpotContributionRepository;
import com.sporty.jackpot.domain.repository.JackpotRepository;
import com.sporty.jackpot.domain.repository.JackpotRewardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JackpotRewardServiceTest {

    @Mock
    private JackpotRepository jackpotRepository;

    @Mock
    private JackpotContributionRepository contributionRepository;

    @Mock
    private JackpotRewardRepository rewardRepository;

    @Mock
    private RewardStrategyFactory rewardStrategyFactory;

    @Mock
    private JackpotDomainMapper jackpotDomainMapper;

    @InjectMocks
    private JackpotRewardService service;

    private static final String BET_ID = "bet-1";
    private static final String USER_ID = "user-1";
    private static final String JACKPOT_ID = "jackpot-1";

    @Test
    void evaluate_whenBetNotProcessed_throwsBetNotYetProcessedException() {
        when(contributionRepository.findByBetId(BET_ID)).thenReturn(Optional.empty());

        assertThrows(BetNotYetProcessedException.class, () -> service.evaluate(BET_ID));
    }

    @Test
    void evaluate_whenAlreadyRewarded_throwsRewardAlreadyGivedException() {
        when(contributionRepository.findByBetId(BET_ID)).thenReturn(Optional.of(buildContributionEntity()));
        when(jackpotRepository.findByIdForUpdate(JACKPOT_ID)).thenReturn(Optional.of(buildJackpotEntity()));
        when(rewardRepository.existsByBetId(BET_ID)).thenReturn(true);

        assertThrows(JackpotRewardAlreadyGivedException.class, () -> service.evaluate(BET_ID));
    }

    @Test
    void evaluate_whenBetWins_savesRewardAndResetsPool() {
        when(contributionRepository.findByBetId(BET_ID)).thenReturn(Optional.of(buildContributionEntity()));
        when(jackpotRepository.findByIdForUpdate(JACKPOT_ID)).thenReturn(Optional.of(buildJackpotEntity()));
        when(rewardRepository.existsByBetId(BET_ID)).thenReturn(false);
        when(jackpotDomainMapper.toDomain(any())).thenReturn(buildJackpot());

        RewardStrategy mockStrategy = mock(RewardStrategy.class);
        when(rewardStrategyFactory.resolve(any())).thenReturn(mockStrategy);
        when(mockStrategy.evaluate(any(), any())).thenReturn(true);

        EvaluateRewardResponse response = service.evaluate(BET_ID);

        assertTrue(response.isWon());
        verify(rewardRepository).save(any(JackpotReward.class));
        verify(jackpotRepository).resetPool(eq(JACKPOT_ID), eq(0));
    }

    @Test
    void evaluate_whenBetLoses_doesNotSaveRewardOrResetPool() {
        when(contributionRepository.findByBetId(BET_ID)).thenReturn(Optional.of(buildContributionEntity()));
        when(jackpotRepository.findByIdForUpdate(JACKPOT_ID)).thenReturn(Optional.of(buildJackpotEntity()));
        when(rewardRepository.existsByBetId(BET_ID)).thenReturn(false);
        when(jackpotDomainMapper.toDomain(any())).thenReturn(buildJackpot());

        RewardStrategy mockStrategy = mock(RewardStrategy.class);
        when(rewardStrategyFactory.resolve(any())).thenReturn(mockStrategy);
        when(mockStrategy.evaluate(any(), any())).thenReturn(false);

        EvaluateRewardResponse response = service.evaluate(BET_ID);

        assertFalse(response.isWon());
        verify(rewardRepository, never()).save(any());
        verify(jackpotRepository, never()).resetPool(any(), any());
    }

    private JackpotContributionEntity buildContributionEntity() {
        JackpotContributionEntity entity = new JackpotContributionEntity();
        entity.setBetId(BET_ID);
        entity.setUserId(USER_ID);
        entity.setJackpotId(JACKPOT_ID);
        entity.setStakeAmount(new BigDecimal("100.00"));
        entity.setContributionAmount(new BigDecimal("10.00"));
        entity.setCurrentJackpotAmount(new BigDecimal("1010.00"));
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    private JackpotEntity buildJackpotEntity() {
        JackpotEntity entity = new JackpotEntity();
        entity.setId(JACKPOT_ID);
        entity.setInitialPoolAmount(new BigDecimal("1000.00"));
        entity.setCurrentPoolAmount(new BigDecimal("1010.00"));
        entity.setContributionStrategyType("FIXED");
        entity.setContributionRate(new BigDecimal("0.10"));
        entity.setRewardStrategyType("FIXED");
        entity.setRewardChance(new BigDecimal("0.50"));
        entity.setVersion(0);
        return entity;
    }

    private Jackpot buildJackpot() {
        return new Jackpot(JACKPOT_ID, new BigDecimal("1000.00"), new BigDecimal("1010.00"),
                ContributionStrategyType.FIXED, new BigDecimal("0.10"), null,
                RewardStrategyType.FIXED, new BigDecimal("0.50"), null, null, 0);
    }
}
