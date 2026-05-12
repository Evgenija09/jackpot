package com.sporty.jackpot.api.controller;

import com.sporty.jackpot.api.dto.request.JackpotCreateRequest;
import com.sporty.jackpot.api.dto.response.JackpotResponse;
import com.sporty.jackpot.domain.service.JackpotService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jackpots")
public class JackpotController {

    private final JackpotService jackpotService;

    public JackpotController(JackpotService jackpotService) {
        this.jackpotService = jackpotService;
    }

    @PostMapping
    public ResponseEntity<JackpotResponse> createJackpot(@Valid @RequestBody JackpotCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jackpotService.createJackpot(request));
    }
}
