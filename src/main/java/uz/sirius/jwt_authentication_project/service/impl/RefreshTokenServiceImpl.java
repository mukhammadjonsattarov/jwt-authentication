package uz.sirius.jwt_authentication_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import uz.sirius.jwt_authentication_project.entity.User;
import uz.sirius.jwt_authentication_project.exception.BadRequestException;
import uz.sirius.jwt_authentication_project.service.RefreshTokenService;
import uz.sirius.jwt_authentication_project.util.JwtTokenProvider;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    private static final long REFRESH_TOKEN_EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000; // 7 kun

    @Override
    public String createRefreshToken(User user) {
        String refreshToken = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(
                "refresh:" + user.getId(),
                refreshToken,
                Duration.ofMillis(REFRESH_TOKEN_EXPIRATION_MS)
        );

        return refreshToken;
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        String userId = findUserIdByToken(refreshToken);
        if (userId == null) {
            throw new BadRequestException("Invalid or expired refresh token");
        }
        User user = new User();
        user.setId(Long.valueOf(userId));
        return jwtTokenProvider.generateRefreshToken(user.getUsername());
    }

    private String findUserIdByToken(String token) {
        for (String key : redisTemplate.keys("refresh:*")) {
            String savedToken = (String) redisTemplate.opsForValue().get(key);
            if (token.equals(savedToken)) {
                return key.replace("refresh:", "");
            }
        }
        return null;
    }
}
