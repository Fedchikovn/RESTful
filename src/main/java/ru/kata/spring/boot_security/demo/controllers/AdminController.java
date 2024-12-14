package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.AdminPageDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserCustomException;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;

import javax.validation.Valid;

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
    public AdminPageDTO adminPage() {
        return new AdminPageDTO(
                userService.showAllUsers(),
                roleService.getAllRoles(),
                userService.getCurrentUser()
        );
    }

    @PostMapping("/add-user")
    public ResponseEntity<HttpStatus> addNewUser(@RequestBody @Valid UserDTO userDTO,
                                                 BindingResult bindingResult) {
        userService.save(userDTO, bindingResult, false);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/edit_user")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid UserDTO userDTO,
                                                 BindingResult bindingResult) {

        userService.update(userDTO, bindingResult);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam int id) {
        userService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> HandleException(UserCustomException e) {
        UserErrorResponse errorResponse = new UserErrorResponse(
                e.getMessage(),
                e.getTimeStamp()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
//    @GetMapping
//    public String adminPage(Model model) {
//        List<User> users = userService.showAllUsers();
//        if (users.isEmpty()) {
//            System.out.println("No users found!");
//        }
//        model.addAttribute("users", users);
//        model.addAttribute("roles", roleService.getAllRoles());
//        model.addAttribute("currentUser", userService.getCurrentUser());
//        return "admin_page";
//    }
//
//    @PostMapping("/edit-user")
//    public String editUser(@ModelAttribute("userToUpdate") User user) {
//        userService.update(user);
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/delete-user")
//    public String deleteUser(@RequestParam("id") int id) {
//        userService.deleteById(id);
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/add-user")
//    public String saveNewUser(@ModelAttribute("user") User user) {
//        userService.save(user);
//        return "redirect:/admin";
//    }
}
