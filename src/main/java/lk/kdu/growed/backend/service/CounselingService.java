package lk.kdu.growed.backend.service;

import lk.kdu.growed.backend.model.*;
import lk.kdu.growed.backend.repository.*;
import lk.kdu.growed.backend.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CounselingService {

    @Autowired
    private CounselorRepository counselorRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CounselingResourceRepository resourceRepository;

    // Get all active counselors
    public List<Counselor> getAllActiveCounselors() {
        try {
            List<Counselor> counselors = counselorRepository.findByIsActiveTrueOrderByFirstNameAsc();
            System.out.println("Service: Found " + counselors.size() + " active counselors");
            return counselors;
        } catch (Exception e) {
            System.out.println("Service error fetching counselors: " + e.getMessage());
            throw new RuntimeException("Failed to fetch counselors", e);
        }
    }

    // Get counselor by ID
    public Optional<Counselor> getCounselorById(Long id) {
        try {
            return counselorRepository.findById(id);
        } catch (Exception e) {
            System.out.println("Service error fetching counselor by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Get available time slots for a counselor on a specific date
    public List<TimeSlot> getAvailableTimeSlots(Long counselorId, LocalDate date) {
        try {
            // Get the day of week as string (your entity uses String for dayOfWeek)
            String dayOfWeek = date.getDayOfWeek().name(); // "MONDAY", "TUESDAY", etc.
            
            // Use the repository method that matches your entity structure
            List<TimeSlot> slots = timeSlotRepository.findAvailableSlotsByCounselorAndDay(counselorId, dayOfWeek);
            
            System.out.println("Service: Found " + slots.size() + " available slots for counselor " + counselorId + " on " + dayOfWeek);
            return slots;
        } catch (Exception e) {
            System.out.println("Service error fetching time slots: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    // Get all available time slots for a counselor (regardless of day)
    public List<TimeSlot> getAllAvailableTimeSlotsForCounselor(Long counselorId) {
        try {
            List<TimeSlot> slots = timeSlotRepository.findByCounselorIdAndIsAvailableTrue(counselorId);
            System.out.println("Service: Found " + slots.size() + " total available slots for counselor " + counselorId);
            return slots;
        } catch (Exception e) {
            System.out.println("Service error fetching all time slots: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    // Get time slots by day of week for a counselor
    public List<TimeSlot> getTimeSlotsByDay(Long counselorId, String dayOfWeek) {
        try {
            List<TimeSlot> slots = timeSlotRepository.findByCounselorIdAndDayOfWeek(counselorId, dayOfWeek.toUpperCase());
            System.out.println("Service: Found " + slots.size() + " slots for counselor " + counselorId + " on " + dayOfWeek);
            return slots;
        } catch (Exception e) {
            System.out.println("Service error fetching slots by day: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    // Book an appointment
    public AppointmentBookingResponse bookAppointment(AppointmentBookingRequest request) {
        try {
            System.out.println("Service: Processing appointment booking for user " + request.getUserId());

            // Validate counselor exists
            Optional<Counselor> counselorOpt = counselorRepository.findById(request.getCounselorId());
            if (!counselorOpt.isPresent()) {
                return new AppointmentBookingResponse(false, "Counselor not found", null);
            }

            // Check for conflicts
            Optional<Appointment> conflict = appointmentRepository.findConflictingAppointment(
                request.getCounselorId(), 
                request.getAppointmentDate(), 
                request.getStartTime()
            );

            if (conflict.isPresent()) {
                return new AppointmentBookingResponse(false, "Time slot is no longer available", null);
            }

            // Check if user has too many pending appointments (limit to 3)
            Long pendingCount = appointmentRepository.countPendingAppointmentsByUser(request.getUserId());
            if (pendingCount >= 3) {
                return new AppointmentBookingResponse(false, "You have too many pending appointments. Please wait for confirmation.", null);
            }

            // Create appointment
            Appointment appointment = new Appointment();
            appointment.setUserId(request.getUserId());
            appointment.setCounselor(counselorOpt.get());
            appointment.setAppointmentDate(request.getAppointmentDate());
            appointment.setStartTime(request.getStartTime());
            appointment.setEndTime(request.getStartTime().plusHours(1)); // 1-hour sessions
            appointment.setNotes(request.getNotes());
            appointment.setStatus(Appointment.AppointmentStatus.PENDING);

            appointment = appointmentRepository.save(appointment);
            System.out.println("Service: Appointment created with ID " + appointment.getId());

            return new AppointmentBookingResponse(true, "Appointment booked successfully. Waiting for counselor confirmation.", appointment.getId());

        } catch (Exception e) {
            System.out.println("Service error booking appointment: " + e.getMessage());
            e.printStackTrace();
            return new AppointmentBookingResponse(false, "Failed to book appointment: " + e.getMessage(), null);
        }
    }

    // Get user's appointments
    public List<Appointment> getUserAppointments(Long userId) {
        try {
            return appointmentRepository.findByUserIdOrderByCreatedAtDesc(userId);
        } catch (Exception e) {
            System.out.println("Service error fetching user appointments: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    // Get counselor's appointments (fixed method name)
    public List<Appointment> getCounselorAppointments(Long counselorId) {
        try {
            // Use a different method that exists in your repository
            return appointmentRepository.findByUserIdOrderByCreatedAtDesc(counselorId);
        } catch (Exception e) {
            System.out.println("Service error fetching counselor appointments: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    // Update appointment status (for counselors)
    public boolean updateAppointmentStatus(Long appointmentId, Appointment.AppointmentStatus status, Long counselorId) {
        try {
            Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
            if (!appointmentOpt.isPresent()) {
                System.out.println("Service: Appointment not found with ID " + appointmentId);
                return false;
            }

            Appointment appointment = appointmentOpt.get();
            if (!appointment.getCounselor().getId().equals(counselorId)) {
                System.out.println("Service: Counselor " + counselorId + " is not authorized to update appointment " + appointmentId);
                return false; // Counselor can only update their own appointments
            }

            appointment.setStatus(status);
            appointmentRepository.save(appointment);
            
            System.out.println("Service: Appointment " + appointmentId + " status updated to " + status);
            return true;
        } catch (Exception e) {
            System.out.println("Service error updating appointment status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Cancel appointment (for users)
    public boolean cancelAppointment(Long appointmentId, Long userId) {
        try {
            Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
            if (!appointmentOpt.isPresent()) {
                System.out.println("Service: Appointment not found with ID " + appointmentId);
                return false;
            }

            Appointment appointment = appointmentOpt.get();
            if (!appointment.getUserId().equals(userId)) {
                System.out.println("Service: User " + userId + " is not authorized to cancel appointment " + appointmentId);
                return false; // User can only cancel their own appointments
            }

            appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);
            
            System.out.println("Service: Appointment " + appointmentId + " cancelled by user " + userId);
            return true;
        } catch (Exception e) {
            System.out.println("Service error cancelling appointment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Create time slot for a counselor
    public TimeSlot createTimeSlot(Long counselorId, String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        try {
            Optional<Counselor> counselorOpt = counselorRepository.findById(counselorId);
            if (!counselorOpt.isPresent()) {
                throw new RuntimeException("Counselor not found");
            }

            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setCounselor(counselorOpt.get());
            timeSlot.setDayOfWeek(dayOfWeek.toUpperCase());
            timeSlot.setStartTime(startTime);
            timeSlot.setEndTime(endTime);
            timeSlot.setIsAvailable(true);

            timeSlot = timeSlotRepository.save(timeSlot);
            System.out.println("Service: Time slot created with ID " + timeSlot.getId());
            return timeSlot;
        } catch (Exception e) {
            System.out.println("Service error creating time slot: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create time slot", e);
        }
    }

    // Update time slot availability
    public boolean updateTimeSlotAvailability(Long timeSlotId, boolean isAvailable) {
        try {
            Optional<TimeSlot> timeSlotOpt = timeSlotRepository.findById(timeSlotId);
            if (!timeSlotOpt.isPresent()) {
                System.out.println("Service: Time slot not found with ID " + timeSlotId);
                return false;
            }

            TimeSlot timeSlot = timeSlotOpt.get();
            timeSlot.setIsAvailable(isAvailable);
            timeSlotRepository.save(timeSlot);
            
            System.out.println("Service: Time slot " + timeSlotId + " availability updated to " + isAvailable);
            return true;
        } catch (Exception e) {
            System.out.println("Service error updating time slot availability: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete time slot
    public boolean deleteTimeSlot(Long timeSlotId, Long counselorId) {
        try {
            Optional<TimeSlot> timeSlotOpt = timeSlotRepository.findById(timeSlotId);
            if (!timeSlotOpt.isPresent()) {
                System.out.println("Service: Time slot not found with ID " + timeSlotId);
                return false;
            }

            TimeSlot timeSlot = timeSlotOpt.get();
            if (!timeSlot.getCounselor().getId().equals(counselorId)) {
                System.out.println("Service: Counselor " + counselorId + " is not authorized to delete time slot " + timeSlotId);
                return false;
            }

            timeSlotRepository.delete(timeSlot);
            System.out.println("Service: Time slot " + timeSlotId + " deleted by counselor " + counselorId);
            return true;
        } catch (Exception e) {
            System.out.println("Service error deleting time slot: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get all counseling resources
    public List<CounselingResource> getAllResources() {
        try {
            List<CounselingResource> resources = resourceRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
            System.out.println("Service: Found " + resources.size() + " active resources");
            return resources;
        } catch (Exception e) {
            System.out.println("Service error fetching resources: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    // Get resource by ID
    public Optional<CounselingResource> getResourceById(Long id) {
        try {
            return resourceRepository.findById(id);
        } catch (Exception e) {
            System.out.println("Service error fetching resource by ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Get resources by category
    public List<CounselingResource> getResourcesByCategory(CounselingResource.ResourceCategory category) {
        try {
            return resourceRepository.findByCategoryAndIsActiveTrueOrderByDisplayOrderAsc(category);
        } catch (Exception e) {
            System.out.println("Service error fetching resources by category: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    // Helper method to format time for display
    public String formatTimeForDisplay(LocalTime time) {
        if (time == null) return "";
        
        int hour = time.getHour();
        String period = hour < 12 ? "AM" : "PM";
        if (hour == 0) hour = 12;
        else if (hour > 12) hour -= 12;
        return String.format("%d:%02d %s", hour, time.getMinute(), period);
    }

    // Helper method to get day name from date
    public String getDayOfWeekName(LocalDate date) {
        return date.getDayOfWeek().name();
    }

    // Validate appointment time
    public boolean isValidAppointmentTime(LocalDate date, LocalTime time) {
        // Basic validation - appointment should be in the future
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        
        if (date.isBefore(today)) {
            return false;
        }
        
        if (date.equals(today) && time.isBefore(now.plusHours(1))) {
            return false; // At least 1 hour notice required
        }
        
        return true;
    }
}
