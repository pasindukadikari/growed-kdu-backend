package lk.kdu.growed.backend.repository;

import lk.kdu.growed.backend.model.ChatSession;
import lk.kdu.growed.backend.model.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    
    Optional<ChatSession> findBySessionId(String sessionId);
    
    List<ChatSession> findByStatusOrderByUpdatedAtDesc(SessionStatus status);
    
    List<ChatSession> findAllByOrderByUpdatedAtDesc();
    
    List<ChatSession> findByIndexNumber(String indexNumber);
    
    long countByStatus(SessionStatus status);
    
    boolean existsBySessionId(String sessionId);
}
