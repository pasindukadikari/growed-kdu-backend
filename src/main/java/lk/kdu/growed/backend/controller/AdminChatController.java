// AdminChatController.java
package lk.kdu.growed.backend.controller;

import lk.kdu.growed.backend.dto.AdminLoginRequest;
import lk.kdu.growed.backend.service.AdminService;
import lk.kdu.growed.backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminChatController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ChatService chatService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody AdminLoginRequest request) {
        try {
            System.out.println("=== ADMIN LOGIN REQUEST ===");
            System.out.println("Request: " + request.toString());
            
            Map<String, Object> response = adminService.authenticateAdmin(
                request.getUsername(), 
                request.getPassword()
            );
            
            System.out.println("Admin login successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Admin login failed: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @GetMapping("/chat-sessions")
    public ResponseEntity<Map<String, Object>> getAllChatSessions() {
        try {
            System.out.println("=== GET ALL CHAT SESSIONS FOR ADMIN REQUEST ===");
            
            Map<String, Object> response = chatService.getAllChatSessionsForAdmin();
            System.out.println("Found " + response.get("totalCount") + " total chat sessions");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error loading chat sessions for admin: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to load chat sessions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/session/{sessionId}/messages")
    public ResponseEntity<Map<String, Object>> getSessionMessages(@PathVariable String sessionId) {
        try {
            System.out.println("=== GET SESSION MESSAGES FOR ADMIN REQUEST ===");
            System.out.println("Session ID: " + sessionId);
            
            Map<String, Object> response = chatService.getSessionMessages(sessionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error loading messages for admin: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to load messages: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/send-message")
    public ResponseEntity<Map<String, Object>> sendAdminMessage(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("=== ADMIN SEND MESSAGE REQUEST ===");
            System.out.println("Request: " + request);
            
            Map<String, Object> message = chatService.sendMessage(
                (String) request.get("sessionId"),
                (String) request.get("messageContent"),
                "ADMIN",
                ((Number) request.get("adminId")).longValue()
            );
            
            System.out.println("Admin message sent successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (Exception e) {
            System.err.println("Error sending admin message: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to send message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
