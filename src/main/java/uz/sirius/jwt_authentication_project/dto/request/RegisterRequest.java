package uz.sirius.jwt_authentication_project.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String fullName;


    @Email(message = "Invalid email format")
    @NotBlank
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}