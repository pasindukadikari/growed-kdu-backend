package lk.kdu.growed.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentBookingResponse {
    private boolean success;
    private String message;
    private Long appointmentId;
}