package lk.kdu.growed.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentBookingRequest {
    
    private Long userId;
    private Long counselorId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private String notes;
    
    // Constructors
    public AppointmentBookingRequest() {}
    
    public AppointmentBookingRequest(Long userId, Long counselorId, LocalDate appointmentDate, 
                                   LocalTime startTime, String notes) {
        this.userId = userId;
        this.counselorId = counselorId;
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.notes = notes;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getCounselorId() {
        return counselorId;
    }
    
    public void setCounselorId(Long counselorId) {
        this.counselorId = counselorId;
    }
    
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }
    
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
