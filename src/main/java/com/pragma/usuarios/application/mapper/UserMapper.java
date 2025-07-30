package com.pragma.usuarios.application.mapper;

import com.pragma.usuarios.application.dto.AuthRequest;
import com.pragma.usuarios.application.dto.CreateUserRequest;
import com.pragma.usuarios.application.dto.CreateUserResponse;
import com.pragma.usuarios.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toUser(CreateUserRequest request);

    CreateUserResponse toUserResponse(User user);

    User toUserLogin(AuthRequest authRequest);
}
