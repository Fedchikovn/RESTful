package ru.kata.spring.boot_security.demo;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.mapper.RoleMapper;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserMapperTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserMapper userMapper;

    @InjectMocks
    private RoleMapper roleMapper;

    public UserMapperTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertToUserDTO() {
        // Подготовка данных
        User user = new User();
        user.setName("John Doe");
        user.setAge(30);
        user.setEmail("john.doe@example.com");

        Role role1 = new Role(1, "ADMIN");
        Role role2 = new Role(2, "USER");
        user.setRoles(Set.of(role1, role2));

        // Эмуляция поведения ModelMapper
        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setName("John Doe");
        expectedUserDTO.setAge(30);
        expectedUserDTO.setEmail("john.doe@example.com");
        expectedUserDTO.setRoles(user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList()));

        when(modelMapper.map(user, UserDTO.class)).thenReturn(expectedUserDTO);

        // Подготовка данных для ролей
        Role role3 = new Role(1, "ADMIN");
        Role role4 = new Role(2, "USER");
        Role role5 = new Role(3, "AUTHOR");
        Set<Role> roles= new HashSet<>(Set.of(role3, role4, role5));

        RoleMapper roleMapper = new RoleMapper(new ModelMapper());

        List<String> actualRoleDTO = roles.stream()
                .map(roleMapper::convertToRoleDTO)
                .collect(Collectors.toList());

        actualRoleDTO.forEach(System.out::println);

        // Проверка
        UserDTO actualUserDTO = userMapper.convertToUserDTO(user);

        System.out.println("Name: " + actualUserDTO.getName());
        System.out.println("Age: " + actualUserDTO.getAge());
        System.out.println("Email: " + actualUserDTO.getEmail());
        System.out.println("Roles: " + actualUserDTO.getRoles());

        assertEquals(expectedUserDTO.getName(), actualUserDTO.getName());
        assertEquals(expectedUserDTO.getAge(), actualUserDTO.getAge());
        assertEquals(expectedUserDTO.getEmail(), actualUserDTO.getEmail());
        assertEquals(expectedUserDTO.getRoles(), actualUserDTO.getRoles());
    }
}
