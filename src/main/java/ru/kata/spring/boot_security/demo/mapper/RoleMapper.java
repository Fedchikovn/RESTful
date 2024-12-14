package ru.kata.spring.boot_security.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.Set;

@Component
public class RoleMapper {
    private final ModelMapper mapper;

    @Autowired
    public RoleMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public String convertToRoleDTO(Role role) {
        return mapper.map(role.getRoleName(), String.class);
    }

    public Role convertToRole(RoleDTO roleDTO) {
        return mapper.map(roleDTO, Role.class);
    }
}
