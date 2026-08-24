package com.janickmediadb.janickmediaapi.repository;

import com.janickmediadb.janickmediaapi.entity.security.RefreshTokenEntity;
import com.janickmediadb.janickmediaapi.entity.security.UsersEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {

    Optional<RefreshTokenEntity> findByToken(String token);

    @Query("SELECT r FROM RefreshTokenEntity r WHERE r.expiryDate <= :date")
    List<RefreshTokenEntity> findAllWithExpirationDateBefore(Instant date);

    @Modifying
    int deleteByUser(UsersEntity user);
}
