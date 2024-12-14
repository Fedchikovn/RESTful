package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
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
    private final UserValidator validator;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper,
                       UserValidator validator) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.validator = validator;
    }

    public List<UserDTO> showAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::convertToUserDTO)
                .collect(Collectors.toList());
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
    public void update(UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserNotUpdatedException(buildErrorMessageFromBindingResult(bindingResult));
        }
        User user = userMapper.convertToUser(userDTO);
        User existUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existUser.setName(user.getName());
        existUser.setAge(user.getAge());
        existUser.setEmail(user.getEmail());
        existUser.setRoles(user.getRoles());
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
