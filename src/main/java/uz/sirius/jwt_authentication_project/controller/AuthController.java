package uz.sirius.jwt_authentication_project.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.sirius.jwt_authentication_project.dto.request.AuthRequest;
import uz.sirius.jwt_authentication_project.dto.response.AuthResponse;
import uz.sirius.jwt_authentication_project.dto.request.RefreshTokenRequest;
import uz.sirius.jwt_authentication_project.dto.request.RegisterRequest;
import uz.sirius.jwt_authentication_project.service.AuthService;
import uz.sirius.jwt_authentication_project.service.RefreshTokenService;
import uz.sirius.jwt_authentication_project.service.TokenBlacklistService;
import uz.sirius.jwt_authentication_project.util.JwtTokenProvider;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            long expirationMillis = jwtTokenProvider.getExpirationDate(token).getTime() - System.currentTimeMillis();
            tokenBlacklistService.blacklistToken(token, expirationMillis);
        }
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String newAccessToken = refreshTokenService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.ok(new AuthResponse(newAccessToken, request.refreshToken(), "Bearer"));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
        boolean verified = authService.verifyAccount(token);
        return verified
                ? ResponseEntity.ok("Account successfully verified! You can now log in.")
                : ResponseEntity.badRequest().body("Invalid or expired token!");
    }

}
