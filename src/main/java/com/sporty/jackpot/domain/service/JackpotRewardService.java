package com.sporty.jackpot.domain.service;

import com.sporty.jackpot.api.dto.response.EvaluateRewardResponse;
import com.sporty.jackpot.domain.model.Jackpot;
import com.sporty.jackpot.domain.model.JackpotReward;
import com.sporty.jackpot.domain.strategy.reward.RewardStrategy;
import com.sporty.jackpot.domain.strategy.reward.RewardStrategyFactory;
import com.sporty.jackpot.exception.BetNotYetProcessedException;
import com.sporty.jackpot.exception.JackpotNotFoundException;
import com.sporty.jackpot.exception.JackpotRewardAlreadyGivedException;
import com.sporty.jackpot.exception.RateLimitExceededException;
import com.sporty.jackpot.exception.ServiceUnavailableException;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotContributionEntity;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotEntity;
import com.sporty.jackpot.infrastructure.persistence.mapper.JackpotDomainMapper;
import com.sporty.jackpot.domain.repository.JackpotContributionRepository;
import com.sporty.jackpot.domain.repository.JackpotRepository;
import com.sporty.jackpot.domain.repository.JackpotRewardRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class JackpotRewardService {

    private static final Logger log = LoggerFactory.getLogger(JackpotRewardService.class);

    private final JackpotRepository jackpotRepository;
    private final JackpotContributionRepository contributionRepository;
    private final JackpotRewardRepository rewardRepository;
    private final RewardStrategyFactory rewardStrategyFactory;
    private final JackpotDomainMapper jackpotDomainMapper;

    public JackpotRewardService(JackpotRepository jackpotRepository,
                                JackpotContributionRepository contributionRepository,
                                JackpotRewardRepository rewardRepository,
                                RewardStrategyFactory rewardStrategyFactory,
                                JackpotDomainMapper jackpotDomainMapper) {
        this.jackpotRepository = jackpotRepository;
        this.contributionRepository = contributionRepository;
        this.rewardRepository = rewardRepository;
        this.rewardStrategyFactory = rewardStrategyFactory;
        this.jackpotDomainMapper = jackpotDomainMapper;
    }

    @Transactional
    @CircuitBreaker(name = "jackpotEvaluate", fallbackMethod = "evaluateFallback")
    @RateLimiter(name = "evaluateRateLimit", fallbackMethod = "rateLimitFallback")
    public EvaluateRewardResponse evaluate(String betId) {
        JackpotContributionEntity contribution = contributionRepository.findByBetId(betId)
                .orElseThrow(() -> new BetNotYetProcessedException(betId));

        JackpotEntity entity = jackpotRepository.findByIdForUpdate(contribution.getJackpotId())
                .orElseThrow(() -> new JackpotNotFoundException(contribution.getJackpotId()));

        if (rewardRepository.existsByBetId(betId)) {
            throw new JackpotRewardAlreadyGivedException(betId);
        }

        Jackpot jackpot = jackpotDomainMapper.toDomain(entity);
        log.debug("Jackpot {} at pool limit: {}", jackpot.getId(), jackpot.isAtPoolLimit());
        RewardStrategy strategy = rewardStrategyFactory.resolve(jackpot);
        boolean won = strategy.evaluate(jackpot.getCurrentPoolAmount(), jackpot.getRewardPoolLimit());

        BigDecimal rewardAmount = jackpot.getCurrentPoolAmount();

        if (won) {
            JackpotReward reward = new JackpotReward();
            reward.setBetId(betId);
            reward.setUserId(contribution.getUserId());
            reward.setJackpotId(contribution.getJackpotId());
            reward.setRewardAmount(rewardAmount);
            reward.setCreatedAt(LocalDateTime.now());

            rewardRepository.save(reward);

            jackpot.reset();
            jackpotRepository.resetPool(jackpot.getId(), jackpot.getVersion());
        }

        return new EvaluateRewardResponse(
                betId,
                contribution.getJackpotId(),
                won,
                won ? rewardAmount : null,
                won ? "Congratulations! You won the jackpot!" : "Better luck next time!"
        );
    }

    public EvaluateRewardResponse evaluateFallback(String betId, Exception e) {
        log.error("Circuit breaker open for jackpot evaluation, betId={}", betId, e);
        throw new ServiceUnavailableException("Jackpot evaluation temporarily unavailable");
    }

    public EvaluateRewardResponse rateLimitFallback(String betId, RequestNotPermitted e) {
        log.warn("Rate limit exceeded for jackpot evaluation, betId={}", betId);
        throw new RateLimitExceededException("Too many requests. Please try again later.");
    }

}
