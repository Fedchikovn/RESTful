package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> performRegistration(@RequestBody @Valid UserDTO userDTO,
                                                          BindingResult bindingResult) {
        userService.save(userDTO, bindingResult, true);
        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @GetMapping("/login")
//    public String loginPage() {
//        return "/auth/login";
//    }
//
//    @GetMapping("/registration")
//    public String registrationPage() {
//        return "auth/registration";
//    }
}
