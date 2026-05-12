package com.sporty.jackpot.api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetPublishResponse {

    private String betId;
    private String status;

    public BetPublishResponse() {
    }

    public BetPublishResponse(String betId, String status) {
        this.betId = betId;
        this.status = status;
    }

}
