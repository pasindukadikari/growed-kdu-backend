// ChatController.java
package lk.kdu.growed.backend.controller;

import lk.kdu.growed.backend.dto.CreateSessionRequest;
import lk.kdu.growed.backend.dto.SendMessageRequest;
import lk.kdu.growed.backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/create-session")
    public ResponseEntity<Map<String, Object>> createChatSession(@RequestBody CreateSessionRequest request) {
        try {
            System.out.println("=== CREATE CHAT SESSION REQUEST ===");
            System.out.println("Request: " + request.toString());
            
            Map<String, Object> session = chatService.createChatSession(
                request.getNickname(), 
                request.getIndexNumber()
            );
            
            System.out.println("Chat session created successfully: " + session.get("sessionId"));
            return ResponseEntity.status(HttpStatus.CREATED).body(session);
        } catch (Exception e) {
            System.err.println("Error creating chat session: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to create chat session: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/send-message")
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody SendMessageRequest request) {
        try {
            System.out.println("=== SEND MESSAGE REQUEST ===");
            System.out.println("Request: " + request.toString());
            
            Map<String, Object> message = chatService.sendMessage(
                request.getSessionId(),
                request.getMessageContent(),
                request.getSenderType(),
                request.getSenderId()
            );
            
            System.out.println("Message sent successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to send message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/messages/{sessionId}")
    public ResponseEntity<Map<String, Object>> getMessages(@PathVariable String sessionId) {
        try {
            System.out.println("=== GET MESSAGES REQUEST ===");
            System.out.println("Session ID: " + sessionId);
            
            Map<String, Object> response = chatService.getSessionMessages(sessionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error loading messages: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to load messages: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/active-sessions")
    public ResponseEntity<Map<String, Object>> getActiveSessions() {
        try {
            System.out.println("=== GET ACTIVE SESSIONS REQUEST ===");
            
            Map<String, Object> response = chatService.getActiveSessions();
            System.out.println("Found " + response.get("count") + " active sessions");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error loading active sessions: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to load active sessions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/mark-read/{sessionId}")
    public ResponseEntity<Map<String, Object>> markMessagesAsRead(
            @PathVariable String sessionId,
            @RequestBody Map<String, String> request) {
        try {
            System.out.println("=== MARK MESSAGES AS READ REQUEST ===");
            System.out.println("Session ID: " + sessionId + ", User Type: " + request.get("userType"));
            
            chatService.markMessagesAsRead(sessionId, request.get("userType"));
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error marking messages as read: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to mark messages as read: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/close-session/{sessionId}")
    public ResponseEntity<Map<String, Object>> closeSession(@PathVariable String sessionId) {
        try {
            System.out.println("=== CLOSE SESSION REQUEST ===");
            System.out.println("Session ID: " + sessionId);
            
            chatService.closeSession(sessionId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Session closed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error closing session: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to close session: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

