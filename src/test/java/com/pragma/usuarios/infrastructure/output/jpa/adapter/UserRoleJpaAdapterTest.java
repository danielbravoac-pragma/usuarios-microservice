package com.pragma.usuarios.infrastructure.output.jpa.adapter;

import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.UserRole;
import com.pragma.usuarios.infrastructure.output.jpa.entity.RoleEntity;
import com.pragma.usuarios.infrastructure.output.jpa.entity.UserEntity;
import com.pragma.usuarios.infrastructure.output.jpa.entity.UserRoleEntity;
import com.pragma.usuarios.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.pragma.usuarios.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.pragma.usuarios.infrastructure.output.jpa.repository.IUserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRoleJpaAdapterTest {
    @Mock
    private IUserRoleRepository userRoleRepository;

    @Mock
    private IUserEntityMapper userEntityMapper;

    @Mock
    private IRoleEntityMapper roleEntityMapper;

    private UserRoleJpaAdapter userRoleJpaAdapter;

    @BeforeEach
    void setUp() {
        userRoleJpaAdapter = new UserRoleJpaAdapter(userRoleRepository, userEntityMapper, roleEntityMapper);
    }

    @Test
    void saveUserRole_shouldMapDomainToEntities_andSaveRelation() {
        // Arrange: Dominio
        User user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");

        Role role = new Role(UserRole.OWNER);
        role.setId(2L);

        // Entidades JPA
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("user@test.com");

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(2L);
        roleEntity.setName(UserRole.OWNER.toString());

        when(userEntityMapper.toUserEntity(user)).thenReturn(userEntity);
        when(roleEntityMapper.toRoleEntity(role)).thenReturn(roleEntity);

        // Act
        userRoleJpaAdapter.saveUserRole(user, role);

        // Assert: Capturar la relación guardada
        ArgumentCaptor<UserRoleEntity> captor = ArgumentCaptor.forClass(UserRoleEntity.class);
        verify(userRoleRepository).save(captor.capture());

        UserRoleEntity savedRelation = captor.getValue();

        assertNotNull(savedRelation.getId());
        assertEquals(1L, savedRelation.getId().getUserId());
        assertEquals(2L, savedRelation.getId().getRoleId());
        assertEquals(userEntity, savedRelation.getUser());
        assertEquals(roleEntity, savedRelation.getRole());

        verify(userEntityMapper).toUserEntity(user);
        verify(roleEntityMapper).toRoleEntity(role);
    }
}
