package com.chatApp.demo.repository;
import java.util.*;

import com.chatApp.demo.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

    public interface MessageRepository extends MongoRepository<ChatMessage, String> {
        List<ChatMessage> findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimestampAsc(
            String senderId1, String receiverId1,
            String senderId2, String receiverId2
        );


        List<ChatMessage> findBySenderIdOrReceiverIdOrderByTimestampDesc(
            String senderId, String receiverId
        );

}