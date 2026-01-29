package com.janick_mediadb.janick_mediaapi.auth;

import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-milliseconds}")
    private int jwtExpirationMs;

    @Value("${app.jwt.refresh-expiration-milliseconds}")
    private int jwtRefreshExpirationMs;

    @Value("${app.jwt.cookie-name}")
    private String jwtCookie;

    @Value("${app.jwt.refresh-cookie-name}")
    private String jwtRefreshCookie;

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername(), jwtExpirationMs);
        return generateCookie(jwtCookie, jwt, "/api", jwtExpirationMs / 1000);
    }

    public ResponseCookie generateJwtCookie(UsersEntity user) {
        String jwt = generateTokenFromUsername(user.getUsername(), jwtExpirationMs);
        return generateCookie(jwtCookie, jwt, "/api", jwtExpirationMs / 1000);
    }

    public ResponseCookie generateRefreshJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername(), jwtRefreshExpirationMs);
        return generateCookie(jwtRefreshCookie, jwt, "/api/auth/refreshtoken", jwtRefreshExpirationMs/ 1000);
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtCookie);
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtRefreshCookie);
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, null).path("/api").build();
    }

    public ResponseCookie getCleanJwtRefreshCookie() {
        return ResponseCookie.from(jwtRefreshCookie, null).path("/api/auth/refreshtoken").build();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().verifyWith(key()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException malformedJwtException) {
            throw new BadRequestException("Invalid JWT Token");
        } catch (ExpiredJwtException expiredJwtException) {
            throw new BadRequestException("Expired JWT Token");
        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new BadRequestException("Unsupported JWT Token");
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new BadRequestException("JWT claims string is null or empty");
        }
    }

    public String generateTokenFromUsername(String username, int expirationMs) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + expirationMs))
                .signWith(key())
                .compact();
    }

    private ResponseCookie generateCookie(String name, String value, String path, int maxAge) {
        return ResponseCookie.from(name, value).path(path).maxAge(maxAge).httpOnly(true).build();
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
}
