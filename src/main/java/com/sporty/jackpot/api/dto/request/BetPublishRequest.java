package com.sporty.jackpot.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BetPublishRequest {

    @NotBlank
    private String betId;

    @NotBlank
    private String userId;

    @NotBlank
    private String jackpotId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    public BetPublishRequest() {
    }

    public BetPublishRequest(String betId, String userId, String jackpotId, BigDecimal amount) {
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.amount = amount;
    }

}
