package uz.sirius.jwt_authentication_project.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank String token,
        @NotBlank String newPassword
) {}
