package lk.kdu.growed.backend.repository;

import lk.kdu.growed.backend.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE a.counselor.id = :counselorId " +
           "AND a.appointmentDate = :date AND a.startTime = :startTime " +
           "AND a.status IN ('PENDING', 'CONFIRMED')")
    Optional<Appointment> findConflictingAppointment(@Param("counselorId") Long counselorId,
                                                   @Param("date") LocalDate date,
                                                   @Param("startTime") LocalTime startTime);

    List<Appointment> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Fixed: Added @Query annotation to specify the exact query
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.userId = :userId AND a.status = 'PENDING'")
    Long countPendingAppointmentsByUser(@Param("userId") Long userId);
}