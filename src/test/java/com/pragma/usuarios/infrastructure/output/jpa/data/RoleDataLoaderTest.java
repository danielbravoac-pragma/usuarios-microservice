package com.pragma.usuarios.infrastructure.output.jpa.data;

import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.infrastructure.output.jpa.entity.RoleEntity;
import com.pragma.usuarios.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.pragma.usuarios.infrastructure.output.jpa.repository.IRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class RoleDataLoaderTest {
    private IRoleRepository roleRepository;
    private IRoleEntityMapper roleEntityMapper;
    private RoleDataLoader roleDataLoader;

    @BeforeEach
    void setUp() {
        roleRepository = mock(IRoleRepository.class);
        roleEntityMapper = mock(IRoleEntityMapper.class);
        roleDataLoader = new RoleDataLoader(roleRepository, roleEntityMapper);
    }

    @Test
    void run_shouldInsertRoles_whenRepositoryIsEmpty() {
        // Arrange
        when(roleRepository.count()).thenReturn(0L);

        // Simular mapeo de roles a entidades
        when(roleEntityMapper.toRoleEntity(any(Role.class))).thenReturn(new RoleEntity());

        // Act
        roleDataLoader.run();

        // Assert: Se deben guardar 4 roles
        verify(roleRepository, times(4)).save(any(RoleEntity.class));
        verify(roleEntityMapper, times(4)).toRoleEntity(any(Role.class));
    }

    @Test
    void run_shouldNotInsertRoles_whenRepositoryHasData() {
        // Arrange
        when(roleRepository.count()).thenReturn(5L);

        // Act
        roleDataLoader.run();

        // Assert: No se deben guardar roles
        verify(roleRepository, never()).save(any(RoleEntity.class));
        verify(roleEntityMapper, never()).toRoleEntity(any(Role.class));
    }
}
