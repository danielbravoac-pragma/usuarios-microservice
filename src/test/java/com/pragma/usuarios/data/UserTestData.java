package com.pragma.usuarios.data;

import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.UserRole;

import java.time.LocalDate;
import java.util.Set;

public class UserTestData {
    public static User validUser() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setLastName("Doe");
        user.setEmail("john@test.com");
        user.setPassword("123456");
        user.setBirthDate(LocalDate.now().minusYears(25));
        user.setRoles(Set.of(new Role(1L,UserRole.OWNER)));
        return user;
    }

    public static User underageUser() {
        User user = validUser();
        user.setBirthDate(LocalDate.now().minusYears(16));
        return user;
    }

    public static User duplicateEmailUser() {
        User user = validUser();
        user.setEmail("duplicate@test.com");
        return user;
    }

    public static User currentAdminUser() {
        User user = validUser();
        user.setId(99L);
        user.setRoles(Set.of(new Role(UserRole.ADMINISTRATOR)));
        return user;
    }
}
