package com.seven.userse.controller;

import com.seven.userse.model.RegistrationRequest;
import com.seven.userse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }
}
