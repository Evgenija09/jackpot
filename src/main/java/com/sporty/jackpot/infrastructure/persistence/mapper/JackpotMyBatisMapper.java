package com.sporty.jackpot.infrastructure.persistence.mapper;

import com.sporty.jackpot.infrastructure.persistence.entity.JackpotEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Optional;

@Mapper
public interface JackpotMyBatisMapper {

    Optional<JackpotEntity> findById(String jackpotId);

    Optional<JackpotEntity> findByIdForUpdate(String jackpotId);

    int updatePoolAmount(@Param("jackpotId") String jackpotId,
                         @Param("newAmount") BigDecimal newAmount,
                         @Param("expectedVersion") Integer expectedVersion);

    int resetPool(@Param("jackpotId") String jackpotId,
                  @Param("expectedVersion") Integer expectedVersion);

    void save(JackpotEntity jackpot);
}
