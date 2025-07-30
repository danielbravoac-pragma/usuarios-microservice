package com.pragma.usuarios.infrastructure.output.jpa.adapter;

import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.infrastructure.output.jpa.entity.UserEntity;
import com.pragma.usuarios.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.pragma.usuarios.infrastructure.output.jpa.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserJpaAdapterTest {
    @Mock
    private IUserRepository userRepository;

    @Mock
    private IUserEntityMapper userEntityMapper;

    private UserJpaAdapter userJpaAdapter;

    @BeforeEach
    void setUp() {
        userJpaAdapter = new UserJpaAdapter(userRepository, userEntityMapper);
    }

    @Test
    void saveUser_shouldMapDomainToEntity_andReturnDomain() {
        // Arrange
        User userDomain = new User();
        userDomain.setEmail("test@mail.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@mail.com");

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(1L);
        savedEntity.setEmail("test@mail.com");

        User savedDomain = new User();
        savedDomain.setId(1L);
        savedDomain.setEmail("test@mail.com");

        when(userEntityMapper.toUserEntity(userDomain)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedEntity);
        when(userEntityMapper.toUser(savedEntity)).thenReturn(savedDomain);

        // Act
        User result = userJpaAdapter.saveUser(userDomain);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@mail.com", result.getEmail());

        verify(userEntityMapper).toUserEntity(userDomain);
        verify(userRepository).save(userEntity);
        verify(userEntityMapper).toUser(savedEntity);
    }

    @Test
    void findByEmail_shouldReturnUser_whenRepositoryFindsEntity() {
        // Arrange
        String email = "found@mail.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        User userDomain = new User();
        userDomain.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.toUser(userEntity)).thenReturn(userDomain);

        // Act
        Optional<User> result = userJpaAdapter.findByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());

        verify(userRepository).findByEmail(email);
        verify(userEntityMapper).toUser(userEntity);
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenRepositoryDoesNotFindEntity() {
        // Arrange
        String email = "notfound@mail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userJpaAdapter.findByEmail(email);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail(email);
        verify(userEntityMapper, never()).toUser(any());
    }
}
