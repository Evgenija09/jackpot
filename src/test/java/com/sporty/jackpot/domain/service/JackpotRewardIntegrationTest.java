package com.sporty.jackpot.domain.service;

import com.sporty.jackpot.api.dto.response.EvaluateRewardResponse;
import com.sporty.jackpot.domain.model.Bet;
import com.sporty.jackpot.domain.model.Money;
import com.sporty.jackpot.exception.BetNotYetProcessedException;
import com.sporty.jackpot.exception.JackpotRewardAlreadyGivedException;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotEntity;
import com.sporty.jackpot.domain.repository.JackpotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.kafka.listener.auto-startup=false")
@Transactional
@Rollback
class JackpotRewardIntegrationTest {

    @Autowired
    private JackpotContributionService jackpotContributionService;

    @Autowired
    private JackpotRewardService jackpotRewardService;

    @Autowired
    private JackpotRepository jackpotRepository;

    @MockBean
    @SuppressWarnings("rawtypes")
    private KafkaTemplate kafkaTemplate;

    private static final String ALWAYS_WIN_JACKPOT_ID = "jackpot-always-win";

    @Test
    void evaluate_whenBetNotYetProcessed_throwsBetNotYetProcessedException() {
        assertThrows(BetNotYetProcessedException.class, () ->
                jackpotRewardService.evaluate("bet-not-processed"));
    }

    @Test
    void evaluate_withAlwaysWinJackpot_winsAndResetsPool() {
        // jackpot-always-win has currentPoolAmount == rewardPoolLimit → always wins
        jackpotContributionService.processContribution(
                buildBet("bet-always-win-1", ALWAYS_WIN_JACKPOT_ID, "50.00"));

        EvaluateRewardResponse response = jackpotRewardService.evaluate("bet-always-win-1");

        assertTrue(response.isWon());
        assertNotNull(response.getRewardAmount());

        BigDecimal poolAfterReset = jackpotRepository.findById(ALWAYS_WIN_JACKPOT_ID)
                .map(JackpotEntity::getCurrentPoolAmount)
                .orElseThrow();

        // pool is reset to initialPoolAmount = 100.0000
        assertEquals(0, new BigDecimal("100.0000").compareTo(poolAfterReset));
    }

    @Test
    void evaluate_calledTwiceForSameBet_secondCallThrowsRewardAlreadyGivenException() {
        jackpotContributionService.processContribution(
                buildBet("bet-double-eval-1", ALWAYS_WIN_JACKPOT_ID, "50.00"));

        jackpotRewardService.evaluate("bet-double-eval-1");

        assertThrows(JackpotRewardAlreadyGivedException.class, () ->
                jackpotRewardService.evaluate("bet-double-eval-1"));
    }

    private Bet buildBet(String betId, String jackpotId, String amount) {
        return new Bet(betId, "user-integration-1", jackpotId, new Money(new BigDecimal(amount)));
    }
}
