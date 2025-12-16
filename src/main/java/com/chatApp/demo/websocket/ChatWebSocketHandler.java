package com.chatApp.demo.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.chatApp.demo.entity.ChatMessage;
import com.chatApp.demo.service.ChatService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {


    private final ChatService chatService;
    private final ObjectMapper objectMapper;


    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();


    public ChatWebSocketHandler(ChatService chatService,ObjectMapper objectMapper) {
      this.chatService = chatService;
      this.objectMapper=objectMapper;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String userId = session.getUri().getQuery().split("=")[1];

        if (!chatService.userExists(userId)) {
            session.close();
            return;
        }

        sessions.put(userId, session);         // tracks who is currently online
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
     throws Exception {


        Map<String, String> payload = objectMapper.readValue(message.getPayload(), Map.class);
        String senderId = payload.get("senderId");
        String receiverId = payload.get("receiverId");   // we extract the receiver and send only to him
        String content = payload.get("content");


        ChatMessage chatMessage = new ChatMessage(senderId, receiverId, content);
        chatService.save(chatMessage);                                   // first save to db


        WebSocketSession receiverSession = sessions.get(receiverId);        // if receiver is online send directly over web sockets
         if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(message.getPayload()));
         }
     }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
       sessions.values().remove(session);
    }
}