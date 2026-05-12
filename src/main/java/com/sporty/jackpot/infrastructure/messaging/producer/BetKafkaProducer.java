package com.sporty.jackpot.infrastructure.messaging.producer;

import com.sporty.jackpot.domain.model.Bet;
import com.sporty.jackpot.domain.service.BetProducer;
import com.sporty.jackpot.infrastructure.config.KafkaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!mock")
public class BetKafkaProducer implements BetProducer {

    private static final Logger log = LoggerFactory.getLogger(BetKafkaProducer.class);

    private final KafkaTemplate<String, Bet> kafkaTemplate;

    public BetKafkaProducer(KafkaTemplate<String, Bet> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(Bet bet) {
        log.info("Publishing bet to Kafka: {}", bet.getBetId());
        try {
            kafkaTemplate.send(KafkaConfig.JACKPOT_BETS_TOPIC, bet.getBetId(), bet)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to deliver bet to Kafka: {}", bet.getBetId(), ex);
                        } else {
                            log.debug("Bet delivered to Kafka: {}", bet.getBetId());
                        }
                    });
            log.info("Bet publish initiated: {}", bet.getBetId());
        } catch (Exception e) {
            log.warn("Kafka unavailable, bet logged but not published: {}", bet.getBetId(), e);
        }
    }
}
