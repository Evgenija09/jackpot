package com.sporty.jackpot.domain.service;

import com.sporty.jackpot.domain.model.Bet;
import com.sporty.jackpot.domain.model.Jackpot;
import com.sporty.jackpot.domain.model.JackpotContribution;
import com.sporty.jackpot.domain.model.Money;
import com.sporty.jackpot.domain.strategy.contribution.ContributionStrategy;
import com.sporty.jackpot.domain.strategy.contribution.ContributionStrategyFactory;
import com.sporty.jackpot.domain.strategy.contribution.ContributionStrategyType;
import com.sporty.jackpot.domain.strategy.reward.RewardStrategyType;
import com.sporty.jackpot.exception.JackpotNotFoundException;
import com.sporty.jackpot.exception.OptimisticLockException;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotEntity;
import com.sporty.jackpot.infrastructure.persistence.mapper.JackpotDomainMapper;
import com.sporty.jackpot.domain.repository.JackpotContributionRepository;
import com.sporty.jackpot.domain.repository.JackpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JackpotContributionServiceTest {

    @Mock
    private JackpotRepository jackpotRepository;

    @Mock
    private JackpotContributionRepository contributionRepository;

    @Mock
    private ContributionStrategyFactory contributionStrategyFactory;

    @Mock
    private JackpotDomainMapper jackpotDomainMapper;

    @InjectMocks
    private JackpotContributionService service;

    private static final String BET_ID = "bet-1";
    private static final String USER_ID = "user-1";
    private static final String JACKPOT_ID = "jackpot-1";

    private Bet bet;

    @BeforeEach
    void setUp() {
        bet = new Bet(BET_ID, USER_ID, JACKPOT_ID, new Money(new BigDecimal("100.00")));
    }

    @Test
    void processContribution_whenBetAlreadyProcessed_skipsProcessing() {
        when(contributionRepository.existsByBetId(BET_ID)).thenReturn(true);

        service.processContribution(bet);

        verify(jackpotRepository, never()).findById(any());
    }

    @Test
    void processContribution_whenJackpotNotFound_throwsJackpotNotFoundException() {
        when(contributionRepository.existsByBetId(BET_ID)).thenReturn(false);
        when(jackpotRepository.findById(JACKPOT_ID)).thenReturn(Optional.empty());

        assertThrows(JackpotNotFoundException.class, () -> service.processContribution(bet));
    }

    @Test
    void processContribution_whenOptimisticLockFails_throwsOptimisticLockException() {
        when(contributionRepository.existsByBetId(BET_ID)).thenReturn(false);
        when(jackpotRepository.findById(JACKPOT_ID)).thenReturn(Optional.of(buildJackpotEntity()));
        when(jackpotDomainMapper.toDomain(any())).thenReturn(buildJackpot());

        ContributionStrategy mockStrategy = mock(ContributionStrategy.class);
        when(contributionStrategyFactory.resolve(any())).thenReturn(mockStrategy);
        when(mockStrategy.calculate(any(), any())).thenReturn(new BigDecimal("10.0000"));
        when(jackpotRepository.updatePoolAmount(any(), any(), any())).thenReturn(0);

        assertThrows(OptimisticLockException.class, () -> service.processContribution(bet));
    }

    @Test
    void processContribution_happyPath_savesContributionAndUpdatesPool() {
        when(contributionRepository.existsByBetId(BET_ID)).thenReturn(false);
        when(jackpotRepository.findById(JACKPOT_ID)).thenReturn(Optional.of(buildJackpotEntity()));
        when(jackpotDomainMapper.toDomain(any())).thenReturn(buildJackpot());

        ContributionStrategy mockStrategy = mock(ContributionStrategy.class);
        BigDecimal expectedContribution = new BigDecimal("10.0000");
        when(contributionStrategyFactory.resolve(any())).thenReturn(mockStrategy);
        when(mockStrategy.calculate(any(), any())).thenReturn(expectedContribution);
        when(jackpotRepository.updatePoolAmount(any(), any(), any())).thenReturn(1);

        service.processContribution(bet);

        ArgumentCaptor<JackpotContribution> captor = ArgumentCaptor.forClass(JackpotContribution.class);
        verify(contributionRepository).save(captor.capture());
        JackpotContribution saved = captor.getValue();
        assertEquals(BET_ID, saved.getBetId());
        assertEquals(0, expectedContribution.compareTo(saved.getContributionAmount()));
    }

    private JackpotEntity buildJackpotEntity() {
        JackpotEntity entity = new JackpotEntity();
        entity.setId(JACKPOT_ID);
        entity.setInitialPoolAmount(new BigDecimal("1000.00"));
        entity.setCurrentPoolAmount(new BigDecimal("1000.00"));
        entity.setContributionStrategyType("FIXED");
        entity.setContributionRate(new BigDecimal("0.10"));
        entity.setRewardStrategyType("FIXED");
        entity.setRewardChance(new BigDecimal("0.50"));
        entity.setVersion(0);
        return entity;
    }

    private Jackpot buildJackpot() {
        return new Jackpot(JACKPOT_ID, new BigDecimal("1000.00"), new BigDecimal("1000.00"),
                ContributionStrategyType.FIXED, new BigDecimal("0.10"), null,
                RewardStrategyType.FIXED, new BigDecimal("0.50"), null, null, 0);
    }
}
