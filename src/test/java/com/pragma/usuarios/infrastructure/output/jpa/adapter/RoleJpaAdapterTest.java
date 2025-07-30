package com.pragma.usuarios.infrastructure.output.jpa.adapter;

import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.UserRole;
import com.pragma.usuarios.infrastructure.exception.DataNotFoundException;
import com.pragma.usuarios.infrastructure.output.jpa.entity.RoleEntity;
import com.pragma.usuarios.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.pragma.usuarios.infrastructure.output.jpa.repository.IRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleJpaAdapterTest {
    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private IRoleEntityMapper roleEntityMapper;

    private RoleJpaAdapter roleJpaAdapter;

    @BeforeEach
    void setUp() {
        roleJpaAdapter = new RoleJpaAdapter(roleRepository, roleEntityMapper);
    }

    @Test
    void findByName_shouldReturnRole_whenRepositoryFindsEntity() {
        // Arrange
        String roleName = "ADMIN";
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName(UserRole.ADMINISTRATOR.toString());

        Role roleDomain = new Role(UserRole.ADMINISTRATOR);
        roleDomain.setId(1L);

        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(roleEntity));
        when(roleEntityMapper.toRole(roleEntity)).thenReturn(roleDomain);

        // Act
        Role result = roleJpaAdapter.findByName(roleName);

        // Assert
        assertNotNull(result);
        assertEquals(UserRole.ADMINISTRATOR, result.getName());
        verify(roleRepository).findByName(roleName);
        verify(roleEntityMapper).toRole(roleEntity);
    }

    @Test
    void findByName_shouldThrowException_whenRoleNotFound() {
        // Arrange
        String roleName = "UNKNOWN";
        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DataNotFoundException.class, () -> roleJpaAdapter.findByName(roleName));

        verify(roleRepository).findByName(roleName);
        verify(roleEntityMapper, never()).toRole(any());
    }
}
