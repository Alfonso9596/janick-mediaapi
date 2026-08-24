package com.janickmediadb.janickmediaapi.auth;

import com.janickmediadb.janickmediaapi.entity.security.RefreshTokenEntity;
import com.janickmediadb.janickmediaapi.exception.BadRequestException;
import com.janickmediadb.janickmediaapi.repository.RefreshTokenRepository;
import com.janickmediadb.janickmediaapi.repository.UserRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {

    @Value("${app.jwt.refresh-expiration-milliseconds}")
    private int jwtRefreshExpirationMs;

    @Value("${server.timezone}")
    private String timezone;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshTokenEntity createRefreshToken(int userId, String token) {
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        Instant currentTime = ZonedDateTime.now(ZoneId.of(timezone)).toInstant();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(currentTime.plusMillis(jwtRefreshExpirationMs));
        refreshToken.setToken(token);

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        Instant currentTime = ZonedDateTime.now(ZoneId.of(timezone)).toInstant();
        if (token.getExpiryDate().compareTo(currentTime) < 0) {
            refreshTokenRepository.delete(token);
            throw new BadRequestException("Refresh token has expired. Please login again");
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(int userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
