package ru.kata.spring.boot_security.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.User;

@Component
public class UserMapper {
    private final ModelMapper mapper;

    @Autowired
    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public UserDTO convertToUserDTO(User user) {
        return mapper.map(user, UserDTO.class);
    }

    public User convertToUser(UserDTO userDTO) {
        return mapper.map(userDTO, User.class);
    }
}
