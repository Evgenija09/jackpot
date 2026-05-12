package com.sporty.jackpot.infrastructure.messaging.producer;

import com.sporty.jackpot.domain.model.Bet;
import com.sporty.jackpot.domain.service.BetProducer;
import com.sporty.jackpot.domain.service.JackpotContributionService;
import com.sporty.jackpot.exception.DuplicateBetException;
import com.sporty.jackpot.exception.JackpotNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("mock")
@Primary
public class MockBetKafkaProducer implements BetProducer {

    private static final Logger log = LoggerFactory.getLogger(MockBetKafkaProducer.class);

    private final JackpotContributionService jackpotContributionService;

    public MockBetKafkaProducer(JackpotContributionService jackpotContributionService) {
        this.jackpotContributionService = jackpotContributionService;
    }

    public void publish(Bet bet) {
        log.info("MOCK: Kafka publish skipped. Payload: betId={}, userId={}, jackpotId={}, amount={}",
                bet.getBetId(), bet.getUserId(), bet.getJackpotId(), bet.getAmount());
        try {
            jackpotContributionService.processContribution(bet);
            log.info("MOCK: Bet processed successfully: {}", bet.getBetId());
        } catch (DuplicateBetException e) {
            log.warn("MOCK: Duplicate bet, skipping: {}", bet.getBetId());
        } catch (JackpotNotFoundException e) {
            log.error("MOCK: Jackpot not found for bet: {}, jackpotId: {}", bet.getBetId(), bet.getJackpotId());
            // Acknowledge silently — mirrors real consumer behaviour
        } catch (Exception e) {
            log.error("MOCK: Failed to process bet: {}", bet.getBetId(), e);
            throw new RuntimeException("Failed to process bet", e);
        }
    }
}
