package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.mapper.RoleMapper;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.security.UserDetail;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserNotUpdatedException;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserValidator validator;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper,
                       RoleMapper roleMapper,
                       UserValidator validator) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.validator = validator;
    }

    public List<UserDTO> showAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(int id) {
        return userMapper.convertToUserDTO(userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with ID %d not found", id))));
    }

    public UserDTO getUserByUsername(String email) {
        return userMapper.convertToUserDTO(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", email))));
    }

    @Transactional
    public void save(UserDTO userDTO, BindingResult bindingResult, boolean isRegistration) {
        //проверка на ошибки валидации
        validator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new UserNotCreatedException(buildErrorMessageFromBindingResult(bindingResult));
        } else {
            User user = userMapper.convertToUser(userDTO);

            user.setRoles(assignRolesToUser(user.getRoles()));

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (isRegistration) {
                authenticateUser(user);
            }

            userRepository.save(user);
        }
    }

    @Transactional
    public void update(int id, UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserNotUpdatedException(buildErrorMessageFromBindingResult(bindingResult));
        }
        User existUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with ID %d not found", id)));
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        existUser.setName(userDTO.getName());
        existUser.setAge(userDTO.getAge());
        existUser.setEmail(userDTO.getEmail());
        existUser.setRoles(userDTO.getRoles().stream().map(roleMapper::convertToRole).collect(Collectors.toSet()));
        userRepository.save(existUser);
    }

    @Transactional
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userMapper.convertToUserDTO(((UserDetail) authentication.getPrincipal()).getUser());
    }

    // ————————————————————————————————————————–——————————————————————————–———————————————————————————————————————————//

    private String buildErrorMessageFromBindingResult(BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMsg.append(error)
                    .append(" — ")
                    .append(error.getDefaultMessage())
                    .append(";");
        }
        return errorMsg.toString();
    }

    private Set<Role> assignRolesToUser(Set<Role> roles) {
        if (roles == null) {
            Role userRole = roleRepository.findByRoleName("USER");
            return Set.of(userRole);
        } else {
            return roles
                    .stream()
                    .map(role -> roleRepository.findById(role.getId())
                            .orElseThrow(() -> new RuntimeException("Role not found")))
                    .collect(Collectors.toSet());
        }
    }

    private void authenticateUser(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword(),
                user.getRoles()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
