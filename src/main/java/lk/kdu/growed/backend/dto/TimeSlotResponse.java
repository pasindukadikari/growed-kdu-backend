package lk.kdu.growed.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotResponse {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String displayTime;
    private boolean available;

    // Constructor with automatic displayTime formatting
    public TimeSlotResponse(Long id, LocalTime startTime, LocalTime endTime, boolean available) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = available;
        this.displayTime = formatTime(startTime);
    }

    // Helper method to format time for display
    private String formatTime(LocalTime time) {
        int hour = time.getHour();
        String period = hour < 12 ? "AM" : "PM";
        if (hour == 0) hour = 12;
        else if (hour > 12) hour -= 12;
        return String.format("%d:00 %s", hour, period);
    }
}