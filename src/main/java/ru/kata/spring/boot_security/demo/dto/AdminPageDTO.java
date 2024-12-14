package ru.kata.spring.boot_security.demo.dto;

import java.util.List;
import java.util.Set;

public class AdminPageDTO {
    private List<UserDTO> users;
    private Set<RoleDTO> roles;
    private UserDTO currentUser;

    public AdminPageDTO(List<UserDTO> users, Set<RoleDTO> roles, UserDTO currentUser) {
        this.users = users;
        this.roles = roles;
        this.currentUser = currentUser;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDTO currentUser) {
        this.currentUser = currentUser;
    }
}
