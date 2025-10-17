package uz.sirius.jwt_authentication_project.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class AuthRequest {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
}