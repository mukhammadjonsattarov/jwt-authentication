package uz.sirius.jwt_authentication_project.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uz.sirius.jwt_authentication_project.dto.response.UserResponse;
import uz.sirius.jwt_authentication_project.entity.User;

import java.util.List;


import java.util.stream.Collectors;

public final class UserMapper {

    private UserMapper() {} // 🔒 private constructor — obyekt yaratilmasin

    public static UserResponse toUserResponse(User user) {
        if (user == null) return null;

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    public static List<UserResponse> toUserResponseList(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserResponse)
                .collect(Collectors.toList());
    }
}

