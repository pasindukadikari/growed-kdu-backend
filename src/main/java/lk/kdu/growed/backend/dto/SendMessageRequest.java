package lk.kdu.growed.backend.dto;

public class SendMessageRequest {
    private String sessionId;
    private String messageContent;
    private String senderType;
    private Long senderId;
    
    public SendMessageRequest() {}
    
    public String getSessionId() { 
        return sessionId; 
    }
    
    public void setSessionId(String sessionId) { 
        this.sessionId = sessionId; 
    }
    
    public String getMessageContent() { 
        return messageContent; 
    }
    
    public void setMessageContent(String messageContent) { 
        this.messageContent = messageContent; 
    }
    
    public String getSenderType() { 
        return senderType; 
    }
    
    public void setSenderType(String senderType) { 
        this.senderType = senderType; 
    }
    
    public Long getSenderId() { 
        return senderId; 
    }
    
    public void setSenderId(Long senderId) { 
        this.senderId = senderId; 
    }
    
    @Override
    public String toString() {
        return "SendMessageRequest{" +
                "sessionId='" + sessionId + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", senderType='" + senderType + '\'' +
                ", senderId=" + senderId +
                '}';
    }
}
