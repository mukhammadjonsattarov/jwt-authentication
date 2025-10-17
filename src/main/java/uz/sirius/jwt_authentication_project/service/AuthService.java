package uz.sirius.jwt_authentication_project.service;

import uz.sirius.jwt_authentication_project.dto.request.AuthRequest;
import uz.sirius.jwt_authentication_project.dto.response.AuthResponse;
import uz.sirius.jwt_authentication_project.dto.request.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(AuthRequest request);
    boolean verifyAccount(String token);
}
