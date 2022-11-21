package com.example.jwttutorial.controller;

import com.example.jwttutorial.dto.UserDto;
import com.example.jwttutorial.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@Slf4j
@RequestMapping("/api")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody UserDto userDto) {
        log.debug("signup");
        return ResponseEntity.ok(userService.signup(userDto));
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
