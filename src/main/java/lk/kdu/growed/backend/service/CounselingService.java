// CounselingService.java
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

    // TODO: Temporarily commented out to get app running
    // @Autowired
    // private AppointmentMessageRepository messageRepository;

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
            // TODO: Fix this method after implementing proper findAvailableSlots
            // TimeSlot.DayOfWeek dayOfWeek = TimeSlot.DayOfWeek.valueOf(date.getDayOfWeek().name());
            // List<TimeSlot> slots = timeSlotRepository.findAvailableSlots(counselorId, dayOfWeek, date);
            
            // Temporary fix: use existing working method
            List<TimeSlot> slots = timeSlotRepository.findByCounselorIdAndIsAvailableTrue(counselorId);
            System.out.println("Service: Found " + slots.size() + " available slots for counselor " + counselorId);
            return slots;
        } catch (Exception e) {
            System.out.println("Service error fetching time slots: " + e.getMessage());
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

            // TODO: Temporarily commented out message creation
            // Create initial message
            // AppointmentMessage message = new AppointmentMessage();
            // message.setAppointment(appointment);
            // message.setSenderType(AppointmentMessage.SenderType.USER);
            // message.setSenderId(request.getUserId());
            // message.setMessage("Appointment booking request for " + 
            //     request.getAppointmentDate() + " at " + request.getStartTime() +
            //     (request.getNotes() != null && !request.getNotes().isEmpty() ? "\nNotes: " + request.getNotes() : ""));
            // message.setMessageType(AppointmentMessage.MessageType.BOOKING_REQUEST);
            // messageRepository.save(message);

            return new AppointmentBookingResponse(true, "Appointment booked successfully. Waiting for counselor confirmation.", appointment.getId());

        } catch (Exception e) {
            System.out.println("Service error booking appointment: " + e.getMessage());
            return new AppointmentBookingResponse(false, "Failed to book appointment: " + e.getMessage(), null);
        }
    }

    // Get user's appointments
    public List<Appointment> getUserAppointments(Long userId) {
        try {
            return appointmentRepository.findByUserIdOrderByCreatedAtDesc(userId);
        } catch (Exception e) {
            System.out.println("Service error fetching user appointments: " + e.getMessage());
            return List.of();
        }
    }

    // Get counselor's appointments
    public List<Appointment> getCounselorAppointments(Long counselorId) {
        try {
            return appointmentRepository.findByUserIdOrderByCreatedAtDesc(counselorId);
        } catch (Exception e) {
            System.out.println("Service error fetching counselor appointments: " + e.getMessage());
            return List.of();
        }
    }

    // Update appointment status (for counselors)
    public boolean updateAppointmentStatus(Long appointmentId, Appointment.AppointmentStatus status, Long counselorId) {
        try {
            Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
            if (!appointmentOpt.isPresent()) {
                return false;
            }

            Appointment appointment = appointmentOpt.get();
            if (!appointment.getCounselor().getId().equals(counselorId)) {
                return false; // Counselor can only update their own appointments
            }

            appointment.setStatus(status);
            appointmentRepository.save(appointment);

            // TODO: Temporarily commented out message creation
            // Create status update message
            // AppointmentMessage message = new AppointmentMessage();
            // message.setAppointment(appointment);
            // message.setSenderType(AppointmentMessage.SenderType.COUNSELOR);
            // message.setSenderId(counselorId);
            // message.setMessage("Appointment " + status.toString().toLowerCase());
            // message.setMessageType(status == Appointment.AppointmentStatus.CONFIRMED ? 
            //     AppointmentMessage.MessageType.CONFIRMATION : AppointmentMessage.MessageType.CANCELLATION);
            // messageRepository.save(message);
            
            System.out.println("Service: Appointment " + appointmentId + " status updated to " + status);

            return true;
        } catch (Exception e) {
            System.out.println("Service error updating appointment status: " + e.getMessage());
            return false;
        }
    }

    // TODO: Temporarily commented out message-related methods
    // Get appointment messages
    // public List<AppointmentMessage> getAppointmentMessages(Long appointmentId) {
    //     try {
    //         return messageRepository.findByAppointmentIdOrderByCreatedAtAsc(appointmentId);
    //     } catch (Exception e) {
    //         System.out.println("Service error fetching appointment messages: " + e.getMessage());
    //         return List.of();
    //     }
    // }

    // Send message to appointment
    // public boolean sendAppointmentMessage(Long appointmentId, String message, AppointmentMessage.SenderType senderType, Long senderId) {
    //     try {
    //         Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
    //         if (!appointmentOpt.isPresent()) {
    //             return false;
    //         }

    //         AppointmentMessage appointmentMessage = new AppointmentMessage();
    //         appointmentMessage.setAppointment(appointmentOpt.get());
    //         appointmentMessage.setSenderType(senderType);
    //         appointmentMessage.setSenderId(senderId);
    //         appointmentMessage.setMessage(message);
    //         appointmentMessage.setMessageType(AppointmentMessage.MessageType.GENERAL);

    //         messageRepository.save(appointmentMessage);
    //         System.out.println("Service: Message sent to appointment " + appointmentId);
    //         return true;
    //     } catch (Exception e) {
    //         System.out.println("Service error sending appointment message: " + e.getMessage());
    //         return false;
    //     }
    // }

    // Get all counseling resources
    public List<CounselingResource> getAllResources() {
        try {
            List<CounselingResource> resources = resourceRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
            System.out.println("Service: Found " + resources.size() + " active resources");
            return resources;
        } catch (Exception e) {
            System.out.println("Service error fetching resources: " + e.getMessage());
            return List.of();
        }
    }

    // Get resource by ID
    public Optional<CounselingResource> getResourceById(Long id) {
        try {
            return resourceRepository.findById(id);
        } catch (Exception e) {
            System.out.println("Service error fetching resource by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Get resources by category
    public List<CounselingResource> getResourcesByCategory(CounselingResource.ResourceCategory category) {
        try {
            return resourceRepository.findByCategoryAndIsActiveTrueOrderByDisplayOrderAsc(category);
        } catch (Exception e) {
            System.out.println("Service error fetching resources by category: " + e.getMessage());
            return List.of();
        }
    }

    // Helper method to format time for display
    public String formatTimeForDisplay(LocalTime time) {
        int hour = time.getHour();
        String period = hour < 12 ? "AM" : "PM";
        if (hour == 0) hour = 12;
        else if (hour > 12) hour -= 12;
        return String.format("%d:00 %s", hour, period);
    }
}