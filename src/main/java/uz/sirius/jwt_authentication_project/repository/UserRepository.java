package uz.sirius.jwt_authentication_project.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.sirius.jwt_authentication_project.entity.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByVerificationToken(String token);
}