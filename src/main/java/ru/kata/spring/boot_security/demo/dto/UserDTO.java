package ru.kata.spring.boot_security.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class UserDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) //TODO: узнать у гпт, почему при этой аннотации происходит ошибка поиска текущего пользователя в консоли браузера
    private int id;
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно иметь длину от 2 до 100 символов")
    private String name;

    @Min(value = 18, message = "Возраст должен быть больше 18 лет")
    private int age = 18;

    @Email(message = "Некорректный email")
    private String email;
    @JsonIgnore
    @JsonProperty
    private String password;

    private List<String> roles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
