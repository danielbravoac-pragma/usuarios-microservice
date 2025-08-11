package com.pragma.usuarios.domain.api;

public interface IPermissionServicePort {
    boolean canCreateOwner();

    void canCreateEmployee();
}
