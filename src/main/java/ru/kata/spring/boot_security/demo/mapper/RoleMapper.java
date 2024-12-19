package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

@Component
public class RoleMapper {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public String convertToRoleDTO(Role role) {
        return role.getRoleName();
    }

    public Role convertToRole(String roleDTO) {
        return roleRepository.findByRoleName(roleDTO);
    }
}
