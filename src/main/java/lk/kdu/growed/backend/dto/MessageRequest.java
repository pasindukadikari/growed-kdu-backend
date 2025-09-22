package lk.kdu.growed.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private String message;
    private String senderType; // "USER" or "COUNSELOR"
    private Long senderId;
}
