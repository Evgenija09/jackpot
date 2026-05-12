package com.sporty.jackpot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaAdmin;

@SpringBootTest
@MockBean(KafkaAdmin.class)
public class JackpotApplicationTests {

    @Test
    void contextLoads() {
    }

}
