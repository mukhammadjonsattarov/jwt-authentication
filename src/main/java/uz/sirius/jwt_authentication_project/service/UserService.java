package uz.sirius.jwt_authentication_project.service;


import uz.sirius.jwt_authentication_project.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
}
