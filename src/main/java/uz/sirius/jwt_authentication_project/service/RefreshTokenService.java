package uz.sirius.jwt_authentication_project.service;

import uz.sirius.jwt_authentication_project.entity.User;

public interface RefreshTokenService {
    String createRefreshToken(User user);
    String refreshAccessToken(String refreshToken);
}
