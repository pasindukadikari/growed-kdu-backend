package lk.kdu.growed.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ChatSession session;
    
    @Column(name = "message_content", nullable = false, columnDefinition = "TEXT")
    private String messageContent;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false)
    private SenderType senderType;
    
    @Column(name = "sender_id")
    private Long senderId;
    
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;
    
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;
    
    // Constructors
    public ChatMessage() {
        this.sentAt = LocalDateTime.now();
        this.isRead = false;
    }
    
    public ChatMessage(ChatSession session, SenderType senderType, Long senderId, String messageContent) {
        this.session = session;
        this.senderType = senderType;
        this.senderId = senderId;
        this.messageContent = messageContent;
        this.sentAt = LocalDateTime.now();
        this.isRead = false;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public ChatSession getSession() {
        return session;
    }
    
    public void setSession(ChatSession session) {
        this.session = session;
    }
    
    public String getMessageContent() {
        return messageContent;
    }
    
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
    
    public SenderType getSenderType() {
        return senderType;
    }
    
    public void setSenderType(SenderType senderType) {
        this.senderType = senderType;
    }
    
    public Long getSenderId() {
        return senderId;
    }
    
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
    
    public LocalDateTime getSentAt() {
        return sentAt;
    }
    
    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
    
    public Boolean getIsRead() {
        return isRead;
    }
    
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}