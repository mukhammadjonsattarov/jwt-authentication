package uz.sirius.jwt_authentication_project.dto.response;

import uz.sirius.jwt_authentication_project.entity.Role;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String username,
        String email,
        Role role,
        LocalDateTime createdAt
) {}
