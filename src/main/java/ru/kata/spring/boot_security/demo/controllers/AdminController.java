package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.showAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping("/current")
    public UserDTO getCurrentUser(Principal principal) {
        return userService.getUserByUsername(principal.getName());
//        return userService.getCurrentUser();
    }

    @GetMapping("/roles")
    public List<String> getRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> addNewUser(@RequestBody @Valid UserDTO userDTO,
                                                 BindingResult bindingResult) {
        userService.save(userDTO, bindingResult, false);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable int id,
                                                 @RequestBody @Valid UserDTO userDTO,
                                                 BindingResult bindingResult) {

        userService.update(id, userDTO, bindingResult);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable int id) {
        userService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @ExceptionHandler
//    private ResponseEntity<UserErrorResponse> HandleException(UserCustomException e) {
//        UserErrorResponse errorResponse = new UserErrorResponse(
//                e.getMessage(),
//                e.getTimeStamp()
//        );
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//    }
}