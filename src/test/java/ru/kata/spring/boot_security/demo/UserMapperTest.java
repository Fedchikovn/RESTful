package ru.kata.spring.boot_security.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.mapper.RoleMapper;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMapper roleMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("John Doe");
        user.setAge(30);
        user.setEmail("john.doe@example.com");

        Role role1 = new Role(1, "ADMIN");
        Role role2 = new Role(2, "USER");
        user.setRoles(Set.of(role1, role2));
    }

    @Test
    void testConvertToUserDTO() {
        // Подготовка данных
        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setName("John Doe");
        expectedUserDTO.setAge(30);
        expectedUserDTO.setEmail("john.doe@example.com");
        expectedUserDTO.setRoles(user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList()));

        // Преобразование через UserMapper
        UserDTO actualUserDTO = userMapper.convertToUserDTO(user);

        System.out.println(actualUserDTO.getName());
        System.out.println(actualUserDTO.getEmail());
        System.out.println(actualUserDTO.getAge());
        actualUserDTO.getRoles().forEach(System.out::println);



        User userReturn = userMapper.convertToUser(actualUserDTO);

        System.out.println(userReturn.getName());
        System.out.println(userReturn.getEmail());
        System.out.println(userReturn.getAge());
        userReturn.getRoles().forEach(System.out::println);

        // Проверка
        assertEquals(expectedUserDTO.getName(), actualUserDTO.getName());
        assertEquals(expectedUserDTO.getAge(), actualUserDTO.getAge());
        assertEquals(expectedUserDTO.getEmail(), actualUserDTO.getEmail());
        assertEquals(expectedUserDTO.getRoles(), actualUserDTO.getRoles());

        // RolesFetching
        Set<Role> roleSet = new HashSet<>(roleRepository.findAll());
        roleSet.forEach(System.out::println);

        List<String> roleList = roleSet.stream().map(roleMapper::convertToRoleDTO).collect(Collectors.toList());
        roleList.forEach(System.out::println);

        Set<Role> roleSetReturn = roleList.stream().map(roleMapper::convertToRole).collect(Collectors.toSet());
        roleSetReturn.forEach(System.out::println);
    }
}
