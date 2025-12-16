package com.chatApp.demo.service;

import com.chatApp.demo.entity.User;
import com.chatApp.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public String register(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            return "User already exists";
        }
        User user = userRepository.save(new User(username, encoder.encode(rawPassword)));
        return user.getId();
    }

    public Optional<String> login(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(user -> encoder.matches(rawPassword, user.getPasswordHash()))
                .map(User::getId);
    }

    public boolean userExists(String userId) {
        return userRepository.findById(userId).isPresent();
    }
}
