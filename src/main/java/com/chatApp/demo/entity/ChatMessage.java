package com.chatApp.demo.entity;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_messages")
public class ChatMessage {
        @Id
        private String id;
        private String senderId;
        private String receiverId;
        private String content;
        private Instant timestamp;


        public ChatMessage(String senderId, String receiverId, String content) {
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.content = content;
            this.timestamp = Instant.now();
        }


        public String getSenderId() {
           return senderId;
        }


        public String getReceiverId() {
           return receiverId;
        }


        public String getContent() {
           return content;
        }


        public Instant getTimestamp() {
           return timestamp;
        }
}