package lk.kdu.growed.backend.repository;

import lk.kdu.growed.backend.model.AppointmentMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentMessageRepository extends JpaRepository<AppointmentMessage, Long> {
    List<AppointmentMessage> findByAppointmentIdOrderByCreatedAtAsc(Long appointmentId);
    List<AppointmentMessage> findByAppointmentIdOrderByCreatedAtDesc(Long appointmentId);
}