package lk.kdu.growed.backend.dto;

public class AppointmentBookingResponse {
    
    private boolean success;
    private String message;
    private Long appointmentId;
    
    // Constructors
    public AppointmentBookingResponse() {}
    
    public AppointmentBookingResponse(boolean success, String message, Long appointmentId) {
        this.success = success;
        this.message = message;
        this.appointmentId = appointmentId;
    }
    
    // Static factory methods for convenience
    public static AppointmentBookingResponse success(String message, Long appointmentId) {
        return new AppointmentBookingResponse(true, message, appointmentId);
    }
    
    public static AppointmentBookingResponse failure(String message) {
        return new AppointmentBookingResponse(false, message, null);
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Long getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
}
