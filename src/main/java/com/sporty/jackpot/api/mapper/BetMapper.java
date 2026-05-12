package com.sporty.jackpot.api.mapper;

import com.sporty.jackpot.api.dto.request.BetPublishRequest;
import com.sporty.jackpot.domain.model.Bet;
import com.sporty.jackpot.domain.model.Money;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {Money.class})
public interface BetMapper {

    @Mapping(target = "amount", expression = "java(new Money(request.getAmount()))")
    Bet toDomain(BetPublishRequest request);
}
