package com.janick_mediadb.janick_mediaapi.auth;

import com.janick_mediadb.janick_mediaapi.entity.security.ERole;
import com.janick_mediadb.janick_mediaapi.entity.security.RefreshTokenEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.RoleEntity;
import com.janick_mediadb.janick_mediaapi.entity.security.UsersEntity;
import com.janick_mediadb.janick_mediaapi.exception.BadRequestException;
import com.janick_mediadb.janick_mediaapi.exception.NotFoundException;
import com.janick_mediadb.janick_mediaapi.repository.RoleRepository;
import com.janick_mediadb.janick_mediaapi.repository.UserRepository;
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
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,  RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

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

        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(userDetails);

        refreshTokenService.createRefreshToken(userDetails.getId(), jwtRefreshCookie.getValue());

        return ResponseEntity.ok()
                .body(new JWTAuthResponse(
                        jwtCookie.getValue(),
                        jwtRefreshCookie.getValue(),
                        userDetails.getId(),
                        userDetails.getUsername(),
                        roles
                ));
    }

    @Override
    public String register(RegisterLoginModel registerModel) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(registerModel.getUsername()))) {
            throw new BadRequestException("Username already exists!");
        }

        UsersEntity user = new UsersEntity();
        user.setUsername(registerModel.getUsername());
        user.setPassword(passwordEncoder.encode(registerModel.getPassword()));
        user.setEnabled(true);

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity userRole = roleRepository.findByName(ERole.USER).orElseThrow(() -> new NotFoundException("Role with name " + ERole.USER + " not found!"));
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public ResponseEntity<String> logout(UserDetailsImpl principal) {
        if (principal.toString().equalsIgnoreCase("anonymousUser")) {
            int userId = principal.getId();
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
    public ResponseEntity<?> refreshToken(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if ((refreshToken != null) && (!refreshToken.isEmpty())) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshTokenEntity::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return ResponseEntity.ok().body(new JWTAuthResponse(
                                jwtCookie.getValue(),
                                refreshToken,
                                user.getId(),
                                user.getUsername(),
                                user.getRoles().stream().map(r -> r.getName().name()).toList()
                        ));
                    })
                    .orElseThrow(() -> new NotFoundException("Refresh token is not in database"));
        }

        return ResponseEntity.badRequest().body("Refresh token is empty");
    }
}
