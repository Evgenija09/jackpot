package com.sporty.jackpot.domain.service;

import com.sporty.jackpot.domain.model.Bet;
import com.sporty.jackpot.domain.model.Money;
import com.sporty.jackpot.exception.JackpotNotFoundException;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotEntity;
import com.sporty.jackpot.domain.repository.JackpotContributionRepository;
import com.sporty.jackpot.domain.repository.JackpotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.kafka.listener.auto-startup=false")
@Transactional
@Rollback
class JackpotContributionIntegrationTest {

    @Autowired
    private JackpotContributionService jackpotContributionService;

    @Autowired
    private JackpotContributionRepository jackpotContributionRepository;

    @Autowired
    private JackpotRepository jackpotRepository;

    @MockBean
    @SuppressWarnings("rawtypes")
    private KafkaTemplate kafkaTemplate;

    private static final String JACKPOT_ID = "jackpot-fixed-fixed";

    @Test
    void processContribution_happyPath_createsContributionAndUpdatesPool() {
        BigDecimal poolBefore = jackpotRepository.findById(JACKPOT_ID)
                .map(JackpotEntity::getCurrentPoolAmount)
                .orElseThrow();

        jackpotContributionService.processContribution(
                buildBet("bet-integration-1", JACKPOT_ID, "100.00"));

        assertTrue(jackpotContributionRepository.existsByBetId("bet-integration-1"));

        BigDecimal poolAfter = jackpotRepository.findById(JACKPOT_ID)
                .map(JackpotEntity::getCurrentPoolAmount)
                .orElseThrow();

        assertTrue(poolAfter.compareTo(poolBefore) > 0);
    }

    @Test
    void processContribution_calledTwiceWithSameBetId_createsOnlyOneContribution() {
        jackpotContributionService.processContribution(
                buildBet("bet-idempotency-1", JACKPOT_ID, "100.00"));
        jackpotContributionService.processContribution(
                buildBet("bet-idempotency-1", JACKPOT_ID, "100.00"));

        assertEquals(1, jackpotContributionRepository.countByBetId("bet-idempotency-1"));
    }

    @Test
    void processContribution_whenJackpotNotFound_throwsException() {
        assertThrows(JackpotNotFoundException.class, () ->
                jackpotContributionService.processContribution(
                        buildBet("bet-unknown-1", "jackpot-does-not-exist", "100.00")));
    }

    private Bet buildBet(String betId, String jackpotId, String amount) {
        return new Bet(betId, "user-integration-1", jackpotId, new Money(new BigDecimal(amount)));
    }
}
