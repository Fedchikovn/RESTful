package ru.kata.spring.boot_security.demo.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Autowired
    private RoleMapper roleMapper;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Настройка конвертера для преобразования Set<Role> в List<String>
        modelMapper.addConverter(new Converter<Set<Role>, List<String>>() {
            public List<String> convert(MappingContext<Set<Role>, List<String>> context) {
                Set<Role> roles = context.getSource();
                return roles.stream()
                        .map(roleMapper::convertToRoleDTO)
                        .collect(Collectors.toList());
            }
        });

        modelMapper.addConverter(new Converter<List<String>, Set<Role>>() {
            public Set<Role> convert(MappingContext<List<String>, Set<Role>> context) {
                List<String> roleNames = context.getSource();
                return roleNames.stream()
                        .map(roleMapper::convertToRole)
                        .collect(Collectors.toSet());
            }
        });

        return modelMapper;
    }
}
