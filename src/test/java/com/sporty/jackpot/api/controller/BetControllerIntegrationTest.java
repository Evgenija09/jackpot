package com.sporty.jackpot.api.controller;

import com.sporty.jackpot.api.dto.request.BetPublishRequest;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        properties = "spring.kafka.listener.auto-startup=false"
)
class BetControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    @SuppressWarnings("rawtypes")
    private KafkaTemplate kafkaTemplate;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        RecordMetadata metadata = new RecordMetadata(
                new TopicPartition("jackpot-bets", 0), 0, 0, 0, 0, 0);
        SendResult<String, Object> sendResult = mock(SendResult.class);
        when(sendResult.getRecordMetadata()).thenReturn(metadata);
        when(kafkaTemplate.send(any(String.class), any(String.class), any()))
                .thenReturn(CompletableFuture.completedFuture(sendResult));
    }

    @Test
    void publishBet_validRequest_returns202() {
        BetPublishRequest request = new BetPublishRequest(
                "bet-ctrl-1", "user-ctrl-1", "jackpot-fixed-fixed", new BigDecimal("100.00"));

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/bets", HttpMethod.POST,
                new HttpEntity<>(request), String.class);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    void publishBet_missingBetId_returns400() {
        BetPublishRequest request = new BetPublishRequest(
                null, "user-ctrl-1", "jackpot-fixed-fixed", new BigDecimal("100.00"));

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/bets", HttpMethod.POST,
                new HttpEntity<>(request), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void publishBet_negativeAmount_returns400() {
        BetPublishRequest request = new BetPublishRequest(
                "bet-ctrl-3", "user-ctrl-1", "jackpot-fixed-fixed", new BigDecimal("-50.00"));

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/bets", HttpMethod.POST,
                new HttpEntity<>(request), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void publishBet_missingJackpotId_returns400() {
        BetPublishRequest request = new BetPublishRequest(
                "bet-ctrl-4", "user-ctrl-1", null, new BigDecimal("100.00"));

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/bets", HttpMethod.POST,
                new HttpEntity<>(request), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
