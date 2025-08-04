package com.pragma.usuarios.application.handler;


import com.pragma.usuarios.application.dto.CreateUserRequest;
import com.pragma.usuarios.application.dto.CreateUserResponse;
import com.pragma.usuarios.application.dto.UserByIdResponse;
import com.pragma.usuarios.application.mapper.UserMapper;
import com.pragma.usuarios.domain.api.IAuthServicePort;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final UserMapper userMapper;
    private final IAuthServicePort authServicePort;


    @Override
    public CreateUserResponse saveOwner(CreateUserRequest createUserRequest) {
        return userMapper.toUserResponse(
                userServicePort.createOwner(
                        userMapper.toUser(createUserRequest)
                ));
    }

    @Override
    public UserByIdResponse findById(Long id) {
        User user = userServicePort.findById(id);
        return new UserByIdResponse(user.getId(), user.getName(), authServicePort.getRoles(user));
    }

    @Override
    public CreateUserResponse saveEmployee(CreateUserRequest createUserRequest) {
        return userMapper.toUserResponse(
                userServicePort.createEmployee(
                        userMapper.toUser(createUserRequest))
        );
    }


}
