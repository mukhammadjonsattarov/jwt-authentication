package uz.sirius.jwt_authentication_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JwtAuthenticationProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtAuthenticationProjectApplication.class, args);
    }

}
