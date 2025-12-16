package com.chatApp.demo.controller;
import java.util.*;
import java.util.Optional;

import com.chatApp.demo.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


        private final AuthService authService;


        public AuthController(AuthService authService) {
        this.authService = authService;
        }


        @PostMapping("/login")
        public Map<String, Object> login(@RequestBody Map<String, String> payload) {
            Optional<String> userId = authService.login(
                payload.get("username"),
                payload.get("password")
            );
            if (userId.isPresent()) {
                return Map.of("success", true, "userId", userId.get());
            }
            return Map.of("success", false);
        }

        @PostMapping("/register")
        public Map<String, Object> register(@RequestBody Map<String, String> payload) {

            String result = authService.register(
                    payload.get("username"),
                    payload.get("password")
            );

            if (result.equals("User already exists")) {
                return Map.of("message", result);
            }
            return Map.of("message", "User registered successfully", "userId", result);
        }

}