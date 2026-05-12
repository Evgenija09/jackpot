package com.sporty.jackpot.domain.service;

import com.sporty.jackpot.api.dto.request.JackpotCreateRequest;
import com.sporty.jackpot.api.dto.response.JackpotResponse;
import com.sporty.jackpot.api.mapper.JackpotMapper;
import com.sporty.jackpot.domain.repository.JackpotRepository;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotEntity;
import org.springframework.stereotype.Service;

@Service
public class JackpotService {

    private final JackpotRepository jackpotRepository;
    private final JackpotMapper jackpotMapper;

    public JackpotService(JackpotRepository jackpotRepository, JackpotMapper jackpotMapper) {
        this.jackpotRepository = jackpotRepository;
        this.jackpotMapper = jackpotMapper;
    }

    public JackpotResponse createJackpot(JackpotCreateRequest request) {
        JackpotEntity entity = jackpotMapper.toEntity(request);
        jackpotRepository.save(entity);
        return jackpotMapper.toResponse(entity);
    }
}
