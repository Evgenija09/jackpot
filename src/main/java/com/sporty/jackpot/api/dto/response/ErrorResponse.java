package com.sporty.jackpot.api.dto.response;

import com.sporty.jackpot.exception.ErrorCode;

public record ErrorResponse(ErrorCode code, String message) {
}
