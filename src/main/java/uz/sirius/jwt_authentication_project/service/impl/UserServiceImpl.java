package uz.sirius.jwt_authentication_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.sirius.jwt_authentication_project.dto.response.UserResponse;
import uz.sirius.jwt_authentication_project.entity.User;
import uz.sirius.jwt_authentication_project.mapper.UserMapper;
import uz.sirius.jwt_authentication_project.repository.UserRepository;
import uz.sirius.jwt_authentication_project.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> all = userRepository.findAll();
        return UserMapper.toUserResponseList(all);
    }

}
