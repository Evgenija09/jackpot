package com.sporty.jackpot.infrastructure.messaging.consumer;

import com.sporty.jackpot.domain.model.Bet;
import com.sporty.jackpot.domain.service.JackpotContributionService;
import com.sporty.jackpot.infrastructure.config.KafkaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class BetKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(BetKafkaConsumer.class);

    private final JackpotContributionService jackpotContributionService;

    public BetKafkaConsumer(JackpotContributionService jackpotContributionService) {
        this.jackpotContributionService = jackpotContributionService;
    }

    @KafkaListener(topics = KafkaConfig.JACKPOT_BETS_TOPIC, groupId = "jackpot-group")
    public void consume(@Payload Bet bet, Acknowledgment ack) {
        log.info("Received bet from Kafka: {}", bet.getBetId());
        try {
            jackpotContributionService.processContribution(bet);
            ack.acknowledge();
            log.info("Successfully processed bet: {}", bet.getBetId());
        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate bet caught at DB level, skipping: {}", bet.getBetId());
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process bet: {}", bet.getBetId(), e);
            // no act — let Kafka retry via DefaultErrorHandler
        }
    }
}
