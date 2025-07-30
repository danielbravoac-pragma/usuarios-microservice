package com.pragma.usuarios.infrastructure.configuration;

import com.pragma.usuarios.application.usecase.PermissionUseCase;
import com.pragma.usuarios.application.usecase.RoleUseCase;
import com.pragma.usuarios.application.usecase.UserRoleUseCase;
import com.pragma.usuarios.application.usecase.UserUseCase;
import com.pragma.usuarios.domain.api.IPermissionServicePort;
import com.pragma.usuarios.domain.api.IRoleServicePort;
import com.pragma.usuarios.domain.api.IUserRoleServicePort;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.spi.IRolePersistencePort;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import com.pragma.usuarios.domain.spi.IUserRolePersistencePort;
import com.pragma.usuarios.infrastructure.output.jpa.adapter.RoleJpaAdapter;
import com.pragma.usuarios.infrastructure.output.jpa.adapter.UserJpaAdapter;
import com.pragma.usuarios.infrastructure.output.jpa.adapter.UserRoleJpaAdapter;
import com.pragma.usuarios.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.pragma.usuarios.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.pragma.usuarios.infrastructure.output.jpa.repository.IRoleRepository;
import com.pragma.usuarios.infrastructure.output.jpa.repository.IUserRepository;
import com.pragma.usuarios.infrastructure.output.jpa.repository.IUserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;
    private final IRoleRepository roleRepository;
    private final IRoleEntityMapper roleEntityMapper;
    private final IUserRoleRepository userRoleRepository;


    @Bean
    public IUserRolePersistencePort userRolePersistencePort(){
        return new UserRoleJpaAdapter(userRoleRepository,userEntityMapper,roleEntityMapper);
    }

    @Bean
    public IUserRoleServicePort userRoleServicePort(){
        return new UserRoleUseCase(userRolePersistencePort());
    }

    @Bean
    public IRolePersistencePort rolePersistencePort(){
        return new RoleJpaAdapter(roleRepository,roleEntityMapper);
    }

    @Bean
    public IRoleServicePort roleServicePort(){
        return new RoleUseCase(rolePersistencePort());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IUserPersistencePort userPersistencePort(){
        return new UserJpaAdapter(userRepository,userEntityMapper);
    }

    @Bean
    public IPermissionServicePort permissionServicePort(){
        return new PermissionUseCase();
    }

    @Bean
    public IUserServicePort userServicePort(){
        return new UserUseCase(userPersistencePort(),passwordEncoder(),permissionServicePort(),roleServicePort(),userRoleServicePort());
    }
}
