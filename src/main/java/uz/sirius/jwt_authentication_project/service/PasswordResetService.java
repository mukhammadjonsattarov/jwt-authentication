package uz.sirius.jwt_authentication_project.service;

import uz.sirius.jwt_authentication_project.dto.request.ForgotPasswordRequest;
import uz.sirius.jwt_authentication_project.dto.request.ResetPasswordRequest;

public interface PasswordResetService {
    String requestPasswordReset(ForgotPasswordRequest request);
    String resetPassword(ResetPasswordRequest request);
}
