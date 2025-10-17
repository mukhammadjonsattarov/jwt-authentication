package uz.sirius.jwt_authentication_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.sirius.jwt_authentication_project.dto.request.ForgotPasswordRequest;
import uz.sirius.jwt_authentication_project.dto.request.ResetPasswordRequest;
import uz.sirius.jwt_authentication_project.entity.PasswordResetToken;
import uz.sirius.jwt_authentication_project.entity.User;
import uz.sirius.jwt_authentication_project.exception.BadRequestException;
import uz.sirius.jwt_authentication_project.exception.ResourceNotFoundException;
import uz.sirius.jwt_authentication_project.repository.PasswordResetTokenRepository;
import uz.sirius.jwt_authentication_project.repository.UserRepository;
import uz.sirius.jwt_authentication_project.service.EmailService;
import uz.sirius.jwt_authentication_project.service.PasswordResetService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public String requestPasswordReset(ForgotPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.email());
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("User not found with email: " + request.email());
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .email(request.email())
                .token(token)
                .expirationDate(LocalDateTime.now().plusMinutes(10))
                .build();

        tokenRepository.save(resetToken);

        String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + token;
        emailService.sendEmail(
                request.email(),
                "Password Reset Request",
                "Click the link below to reset your password:\n" + resetLink
        );

        return "Password reset link sent to your email";

    }

    @Override
    public String resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = tokenRepository.findByToken(request.token())
                .orElseThrow(() -> new BadRequestException("Invalid token"));

        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token expired");
        }

        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        tokenRepository.delete(resetToken);
        return "Password reset successful";
    }
}
