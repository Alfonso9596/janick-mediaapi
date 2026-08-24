package com.janickmediadb.janickmediaapi.schedules;

import com.janickmediadb.janickmediaapi.entity.security.RefreshTokenEntity;
import com.janickmediadb.janickmediaapi.repository.RefreshTokenRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenScheduler {

    @Value("${server.timezone}")
    private String timezone;

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenScheduler(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void clearRefreshTokens() {
        Instant currentTime = ZonedDateTime.now(ZoneId.of(timezone)).toInstant();
        List<RefreshTokenEntity> refreshTokens = refreshTokenRepository.findAllWithExpirationDateBefore(currentTime.minusMillis(86400000));
        refreshTokenRepository.deleteAll(refreshTokens);
    }
}
