// ChatService.java
package lk.kdu.growed.backend.service;

import lk.kdu.growed.backend.model.*;
import lk.kdu.growed.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public Map<String, Object> createChatSession(String nickname, String indexNumber) {
        try {
            // Generate unique session ID
            String sessionId = "chat_" + UUID.randomUUID().toString().replace("-", "");
            
            // Create chat session
            ChatSession session = new ChatSession(sessionId, indexNumber, nickname);
            ChatSession savedSession = chatSessionRepository.save(session);
            
            System.out.println("Created chat session: " + sessionId + " for index: " + indexNumber + " with nickname: " + nickname);
            
            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("sessionId", savedSession.getSessionId());
            response.put("nickname", savedSession.getNickname());
            response.put("indexNumber", savedSession.getIndexNumber());
            response.put("status", savedSession.getStatus().toString());
            response.put("createdAt", savedSession.getCreatedAt());
            
            return response;
        } catch (Exception e) {
            System.err.println("Error creating chat session: " + e.getMessage());
            throw new RuntimeException("Failed to create chat session: " + e.getMessage());
        }
    }

    public Map<String, Object> sendMessage(String sessionId, String messageContent, String senderType, Long senderId) {
        try {
            // Find session
            ChatSession session = chatSessionRepository.findBySessionId(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));

            // Create message
            ChatMessage message = new ChatMessage(
                session, 
                SenderType.valueOf(senderType), 
                senderId, 
                messageContent
            );
            
            ChatMessage savedMessage = chatMessageRepository.save(message);
            
            // Update session timestamp
            session.setUpdatedAt(LocalDateTime.now());
            chatSessionRepository.save(session);
            
            System.out.println("Message sent in session " + sessionId + " by " + senderType + ": " + messageContent);
            
            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("id", savedMessage.getId());
            response.put("messageContent", savedMessage.getMessageContent());
            response.put("senderType", savedMessage.getSenderType().toString());
            response.put("sentAt", savedMessage.getSentAt());
            
            return response;
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            throw new RuntimeException("Failed to send message: " + e.getMessage());
        }
    }

    public Map<String, Object> getSessionMessages(String sessionId) {
        try {
            ChatSession session = chatSessionRepository.findBySessionId(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));

            List<ChatMessage> messages = chatMessageRepository
                    .findBySessionOrderBySentAtAsc(session);

            List<Map<String, Object>> messageList = messages.stream()
                    .map(this::convertMessageToMap)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("sessionId", sessionId);
            response.put("nickname", session.getNickname());
            response.put("indexNumber", session.getIndexNumber());
            response.put("messages", messageList);
            
            return response;
        } catch (Exception e) {
            System.err.println("Error getting messages: " + e.getMessage());
            throw new RuntimeException("Failed to get messages: " + e.getMessage());
        }
    }

    public Map<String, Object> getActiveSessions() {
        try {
            List<ChatSession> activeSessions = chatSessionRepository
                    .findByStatusOrderByUpdatedAtDesc(SessionStatus.ACTIVE);

            List<Map<String, Object>> sessionList = activeSessions.stream()
                    .map(this::convertSessionToMap)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("sessions", sessionList);
            response.put("count", sessionList.size());
            
            return response;
        } catch (Exception e) {
            System.err.println("Error getting active sessions: " + e.getMessage());
            throw new RuntimeException("Failed to get active sessions: " + e.getMessage());
        }
    }

    public Map<String, Object> getAllChatSessionsForAdmin() {
        try {
            List<ChatSession> allSessions = chatSessionRepository
                    .findAllByOrderByUpdatedAtDesc();

            List<Map<String, Object>> sessionList = allSessions.stream()
                    .map(session -> {
                        Map<String, Object> sessionMap = convertSessionToMap(session);
                        
                        // Add unread message count for admin
                        long unreadCount = chatMessageRepository
                                .countBySessionAndSenderTypeAndIsRead(session, SenderType.STUDENT, false);
                        sessionMap.put("unreadCount", unreadCount);
                        
                        // Add last message
                        Optional<ChatMessage> lastMessage = chatMessageRepository
                                .findTopBySessionOrderBySentAtDesc(session);
                        if (lastMessage.isPresent()) {
                            sessionMap.put("lastMessage", lastMessage.get().getMessageContent());
                            sessionMap.put("lastMessageAt", lastMessage.get().getSentAt());
                        }
                        
                        return sessionMap;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("sessions", sessionList);
            response.put("totalCount", sessionList.size());
            
            return response;
        } catch (Exception e) {
            System.err.println("Error getting chat sessions for admin: " + e.getMessage());
            throw new RuntimeException("Failed to get chat sessions: " + e.getMessage());
        }
    }

    public void markMessagesAsRead(String sessionId, String userType) {
        try {
            ChatSession session = chatSessionRepository.findBySessionId(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));

            SenderType senderTypeToMark = userType.equals("ADMIN") ? SenderType.STUDENT : SenderType.ADMIN;
            
            List<ChatMessage> unreadMessages = chatMessageRepository
                    .findBySessionAndSenderTypeAndIsRead(session, senderTypeToMark, false);
            
            unreadMessages.forEach(message -> message.setIsRead(true));
            chatMessageRepository.saveAll(unreadMessages);
            
            System.out.println("Marked " + unreadMessages.size() + " messages as read in session " + sessionId);
            
        } catch (Exception e) {
            System.err.println("Error marking messages as read: " + e.getMessage());
            throw new RuntimeException("Failed to mark messages as read: " + e.getMessage());
        }
    }

    public void closeSession(String sessionId) {
        try {
            ChatSession session = chatSessionRepository.findBySessionId(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));
            
            session.setStatus(SessionStatus.CLOSED);
            chatSessionRepository.save(session);
            
            System.out.println("Closed chat session: " + sessionId);
            
        } catch (Exception e) {
            System.err.println("Error closing session: " + e.getMessage());
            throw new RuntimeException("Failed to close session: " + e.getMessage());
        }
    }

    private Map<String, Object> convertMessageToMap(ChatMessage message) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", message.getId());
        map.put("messageContent", message.getMessageContent());
        map.put("senderType", message.getSenderType().toString());
        map.put("senderId", message.getSenderId());
        map.put("sentAt", message.getSentAt());
        map.put("isRead", message.getIsRead());
        return map;
    }

    private Map<String, Object> convertSessionToMap(ChatSession session) {
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", session.getSessionId());
        map.put("nickname", session.getNickname());
        map.put("indexNumber", session.getIndexNumber());
        map.put("status", session.getStatus().toString());
        map.put("createdAt", session.getCreatedAt());
        map.put("updatedAt", session.getUpdatedAt());
        return map;
    }
}
