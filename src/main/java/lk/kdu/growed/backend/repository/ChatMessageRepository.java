package lk.kdu.growed.backend.repository;

import lk.kdu.growed.backend.model.ChatMessage;
import lk.kdu.growed.backend.model.ChatSession;
import lk.kdu.growed.backend.model.SenderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findBySessionOrderBySentAtAsc(ChatSession session);
    
    long countBySessionAndSenderTypeAndIsRead(ChatSession session, SenderType senderType, boolean isRead);
    
    Optional<ChatMessage> findTopBySessionOrderBySentAtDesc(ChatSession session);
    
    List<ChatMessage> findBySessionAndSenderTypeAndIsRead(ChatSession session, SenderType senderType, boolean isRead);
}