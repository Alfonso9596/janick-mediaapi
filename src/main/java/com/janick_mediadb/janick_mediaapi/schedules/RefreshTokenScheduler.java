package com.janick_mediadb.janick_mediaapi.schedules;

import com.janick_mediadb.janick_mediaapi.entity.security.RefreshTokenEntity;
import com.janick_mediadb.janick_mediaapi.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class RefreshTokenScheduler {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 12 * * *")
    public void clearRefreshTokens() {
        List<RefreshTokenEntity>  refreshTokens = refreshTokenRepository.findAllWithExpirationDateBefore(Instant.now().minusMillis(86400000));
        refreshTokenRepository.deleteAll(refreshTokens);
    }
}
