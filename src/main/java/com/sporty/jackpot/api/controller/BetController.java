package com.sporty.jackpot.api.controller;

import com.sporty.jackpot.api.dto.request.BetPublishRequest;
import com.sporty.jackpot.api.dto.response.BetPublishResponse;
import com.sporty.jackpot.api.dto.response.EvaluateRewardResponse;
import com.sporty.jackpot.api.mapper.BetMapper;
import com.sporty.jackpot.domain.model.Bet;
import com.sporty.jackpot.domain.service.BetService;
import com.sporty.jackpot.domain.service.JackpotRewardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bets")
public class BetController {

    private final BetService betService;
    private final JackpotRewardService jackpotRewardService;
    private final BetMapper betMapper;

    public BetController(BetService betService, JackpotRewardService jackpotRewardService, BetMapper betMapper) {
        this.betService = betService;
        this.jackpotRewardService = jackpotRewardService;
        this.betMapper = betMapper;
    }

    @PostMapping
    public ResponseEntity<BetPublishResponse> publishBet(@Valid @RequestBody BetPublishRequest request) {
        Bet bet = betMapper.toDomain(request);
        return ResponseEntity.accepted().body(betService.publishBet(bet));
    }

    @PostMapping("/{betId}/evaluate")
    public ResponseEntity<EvaluateRewardResponse> evaluateReward(@PathVariable String betId) {
        return ResponseEntity.ok(jackpotRewardService.evaluate(betId));
    }
}
