package uz.sirius.jwt_authentication_project.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.sirius.jwt_authentication_project.dto.request.AuthRequest;
import uz.sirius.jwt_authentication_project.dto.response.AuthResponse;
import uz.sirius.jwt_authentication_project.dto.request.RegisterRequest;
import uz.sirius.jwt_authentication_project.entity.Role;
import uz.sirius.jwt_authentication_project.entity.User;
import uz.sirius.jwt_authentication_project.exception.ApiException;
import uz.sirius.jwt_authentication_project.exception.BadRequestException;
import uz.sirius.jwt_authentication_project.exception.ResourceNotFoundException;
import uz.sirius.jwt_authentication_project.repository.UserRepository;
import uz.sirius.jwt_authentication_project.service.AuthService;
import uz.sirius.jwt_authentication_project.service.EmailService;
import uz.sirius.jwt_authentication_project.service.RefreshTokenService;
import uz.sirius.jwt_authentication_project.util.JwtTokenProvider;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        String verificationToken = UUID.randomUUID().toString();

        User user = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .verificationToken(verificationToken)
                .build();

        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        return new AuthResponse(null, null, "Check your email to verify your account");
    }



    @Override
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.isEnabled()) {
            throw new ApiException("Please verify your email before login");
        }
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }

    @Override
    public boolean verifyAccount(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid or expired token"));

        user.setEnabled(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        return true;
    }

}
