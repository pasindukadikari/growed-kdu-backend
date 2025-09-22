package lk.kdu.growed.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentBookingRequest {
    private Long userId;
    private Long counselorId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private String notes;
}