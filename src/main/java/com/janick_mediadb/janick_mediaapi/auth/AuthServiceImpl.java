package com.janick_mediadb.janick_mediaapi.auth;

import com.janick_mediadb.janick_mediaapi.entity.security.RefreshTokenEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.RoleEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.NotFoundException;
import com.janick_mediadb.janick_mediaapi.repository.RoleRepository;
import com.janick_mediadb.janick_mediaapi.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public ResponseEntity<JWTAuthResponse> login(RegisterLoginModel loginModel) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginModel.getUsername(), loginModel.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new JWTAuthResponse(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        roles
                ));
    }

    @Override
    public String register(RegisterLoginModel registerModel) {
        if (userRepository.existsByUsername(registerModel.getUsername())) {
            throw new BadRequestException("Username already exists!");
        }

        UsersEntity user = new UsersEntity();
        user.setUsername(registerModel.getUsername());
        user.setPassword(passwordEncoder.encode(registerModel.getPassword()));

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity userRole = roleRepository.findByName("USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public ResponseEntity<String> logout(Object principal) {
        if (!Objects.equals(principal.toString(), "anonymousUser")) {
            int userId = ((UserDetailsImpl) principal).getId();
            refreshTokenService.deleteByUserId(userId);
        }

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body("You've been signed out");
    }

    @Override
    public ResponseEntity<String> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (!refreshToken.isEmpty())) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshTokenEntity::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body("Token is refresh successfully");
                    })
                    .orElseThrow(() -> new NotFoundException("Refresh token is not in database"));
        }

        return ResponseEntity.badRequest().body("Refresh token is empty");
    }
}
