package com.pragma.usuarios.infrastructure.output.jpa.mapper;


import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.infrastructure.output.jpa.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRoleEntityMapper {
    RoleEntity toRoleEntity(Role user);

    Role toRole(RoleEntity roleEntity);
}
