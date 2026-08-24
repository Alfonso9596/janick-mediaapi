package com.janickmediadb.janickmediaapi.auth;

import com.janickmediadb.janickmediaapi.entity.security.RefreshTokenEntity;
import com.janickmediadb.janickmediaapi.entity.security.RoleEntity;
import com.janickmediadb.janickmediaapi.entity.security.RoleEnum;
import com.janickmediadb.janickmediaapi.entity.security.UsersEntity;
import com.janickmediadb.janickmediaapi.exception.BadRequestException;
import com.janickmediadb.janickmediaapi.exception.NotFoundException;
import com.janickmediadb.janickmediaapi.input.admin.PasswordChangeInput;
import com.janickmediadb.janickmediaapi.input.admin.UserInput;
import com.janickmediadb.janickmediaapi.repository.RoleRepository;
import com.janickmediadb.janickmediaapi.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public ResponseEntity<JwtAuthResponse> login(LoginModel loginModel) {
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
                .body(new JwtAuthResponse(
                        jwtCookie.getValue(),
                        jwtRefreshCookie.getValue(),
                        userDetails.getId(),
                        userDetails.getUsername(),
                        roles
                ));
    }

    @Override
    public ResponseEntity<String> register(UserInput userInput) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(userInput.getUsername()))) {
            throw new BadRequestException("Username already exists!");
        }

        UsersEntity user = new UsersEntity();
        user.setUsername(userInput.getUsername());
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));
        user.setEnabled(true);

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity userRole = roleRepository.findByName(RoleEnum.USER.getValue()).orElseThrow(() -> new NotFoundException("Role with name " + RoleEnum.USER + " not found!"));
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
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
    public ResponseEntity<String> updatePassword(UserDetailsImpl principal, PasswordChangeInput passwordChangeInput) {
        UsersEntity user = userRepository.findByUsername(principal.getUsername()).orElseThrow(() -> new NotFoundException("Username not found!"));
        if (!passwordEncoder.matches(passwordChangeInput.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password does not match old password!");
        }

        user.setPassword(passwordEncoder.encode(passwordChangeInput.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok()
                .body("Password changed successfully");
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

                        return ResponseEntity.ok().body(new JwtAuthResponse(
                                jwtCookie.getValue(),
                                refreshToken,
                                user.getId(),
                                user.getUsername(),
                                user.getRoles().stream().map(RoleEntity::getName).toList()
                        ));
                    })
                    .orElseThrow(() -> new NotFoundException("Refresh token is not in database"));
        }

        return ResponseEntity.badRequest().body("Refresh token is empty");
    }
}
