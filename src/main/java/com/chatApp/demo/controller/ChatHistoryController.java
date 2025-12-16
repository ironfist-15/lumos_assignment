package com.chatApp.demo.controller;
import com.chatApp.demo.ConversationDTO;
import com.chatApp.demo.entity.ChatMessage;
import com.chatApp.demo.repository.UserRepository;
import com.chatApp.demo.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.*;



@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {

    private final ChatService chatService;
    private final UserRepository userRepository;

    public ChatHistoryController(ChatService chatService, UserRepository userRepository) {
        this.chatService = chatService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{userId1}/history/{userId2}")
    public List<ChatMessage> history(@PathVariable String userId1, @PathVariable String userId2) {
      return chatService.getConversation(userId1, userId2);
    }

    @GetMapping("/{id}/search")
    public Map<String, Object> search(@RequestParam String username){
        return userRepository.findByUsername(username)
                .map(user -> Map.<String, Object>of(
                        "username", username,
                        "userId", user.getId(),
                        "exists", true
                ))
                .orElseGet(() -> Map.of(
                        "username", username,
                        "exists", false
                ));
    }

    @GetMapping("/{userId}/home")
    public List<ConversationDTO> conversations(@PathVariable String userId) {
        return chatService.getUserConversations(userId);
    }
}