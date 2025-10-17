package uz.sirius.jwt_authentication_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.sirius.jwt_authentication_project.entity.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
