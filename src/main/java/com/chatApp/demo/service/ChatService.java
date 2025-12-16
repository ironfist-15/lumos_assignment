package com.chatApp.demo.service;

import java.util.*;

import com.chatApp.demo.ConversationDTO;
import com.chatApp.demo.entity.ChatMessage;
import com.chatApp.demo.repository.MessageRepository;
import com.chatApp.demo.repository.UserRepository;
import com.chatApp.demo.entity.User;
import org.springframework.stereotype.Service;

@Service
public class ChatService {


    private final MessageRepository messageRepository;
    private  final UserRepository userRepository;

    public ChatService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public void save(ChatMessage message) {
       messageRepository.save(message);
    }


    public List<ChatMessage> getConversation(String userId1, String userId2) {
        return messageRepository
            .findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimestampAsc(
                userId1, userId2,
                userId2, userId1
            );
    }


    public boolean userExists(String userId) {
        return userRepository.findById(userId).isPresent();
    }

    public List<ConversationDTO> getUserConversations(String userId) {

    List<ChatMessage> messages =
            messageRepository.findBySenderIdOrReceiverIdOrderByTimestampDesc(userId, userId);

    Map<String, ConversationDTO> conversations = new LinkedHashMap<>();

    for (ChatMessage msg : messages) {

        String otherUserId = msg.getSenderId().equals(userId)
                ? msg.getReceiverId()
                : msg.getSenderId();

        // fetch username safely
        String otherUsername = userRepository.findById(otherUserId)
                .map(User::getUsername)
                .orElse(otherUserId); // fallback to id if user not found

        // first occurrence = latest message due to ordering
        conversations.putIfAbsent(
                otherUserId,
                new ConversationDTO(
                        otherUserId,
                        otherUsername,
                        msg.getContent(),
                        msg.getTimestamp()
                )
        );
    }

    return new ArrayList<>(conversations.values());
}

}