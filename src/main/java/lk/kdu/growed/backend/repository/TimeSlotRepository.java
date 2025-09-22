package lk.kdu.growed.backend.repository;

import lk.kdu.growed.backend.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    
    List<TimeSlot> findByCounselorId(Long counselorId);
    
    List<TimeSlot> findByCounselorIdAndIsAvailableTrue(Long counselorId);
    
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.counselor.id = :counselorId AND ts.dayOfWeek = :dayOfWeek AND ts.isAvailable = true")
    List<TimeSlot> findAvailableSlotsByCounselorAndDay(@Param("counselorId") Long counselorId, @Param("dayOfWeek") String dayOfWeek);
    
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.counselor.id = :counselorId AND ts.startTime >= :startTime AND ts.endTime <= :endTime")
    List<TimeSlot> findSlotsByTimeRange(@Param("counselorId") Long counselorId, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    List<TimeSlot> findAvailableSlots(Long counselorId, lk.kdu.growed.backend.model.TimeSlot.DayOfWeek dayOfWeek,
            LocalDate date);

    // TODO: Fix this method - commented out to get app running
    // The 'date' field doesn't exist in TimeSlot entity
    // @Query("SELECT ts FROM TimeSlot ts WHERE ts.counselor.id = :counselorId " +
    //        "AND ts.dayOfWeek = :dayOfWeek " +
    //        "AND ts.isAvailable = true " +
    //        "AND ts.date = :date")
    // List<TimeSlot> findAvailableSlots(@Param("counselorId") Long counselorId, 
    //                                  @Param("dayOfWeek") lk.kdu.growed.backend.model.TimeSlot.DayOfWeek dayOfWeek,
    //                                  @Param("date") LocalDate date);
}