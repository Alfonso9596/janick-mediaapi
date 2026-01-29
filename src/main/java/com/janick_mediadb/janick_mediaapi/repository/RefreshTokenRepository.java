package com.janick_mediadb.janick_mediaapi.repository;

import com.janick_mediadb.janick_mediaapi.entity.security.RefreshTokenEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {

    Optional<RefreshTokenEntity> findByToken(String token);

    @Query("SELECT r FROM RefreshTokenEntity r WHERE r.expiryDate <= :date")
    List<RefreshTokenEntity> findAllWithExpirationDateBefore(Instant date);

    @Modifying
    int deleteByUser(UsersEntity user);
}
