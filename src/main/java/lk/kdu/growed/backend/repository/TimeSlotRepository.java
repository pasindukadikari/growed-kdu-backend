package lk.kdu.growed.backend.repository;

import lk.kdu.growed.backend.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    
    // Methods using the relationship path (counselor.id)
    List<TimeSlot> findByCounselorId(Long counselorId);
    
    List<TimeSlot> findByCounselorIdAndIsAvailableTrue(Long counselorId);
    
    List<TimeSlot> findByIsAvailableTrue();
    
    List<TimeSlot> findByDayOfWeek(String dayOfWeek);
    
    List<TimeSlot> findByCounselorIdAndDayOfWeek(Long counselorId, String dayOfWeek);
    
    // Custom queries using the correct relationship path
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.counselor.id = :counselorId AND ts.dayOfWeek = :dayOfWeek AND ts.isAvailable = true")
    List<TimeSlot> findAvailableSlotsByCounselorAndDay(@Param("counselorId") Long counselorId, @Param("dayOfWeek") String dayOfWeek);
    
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.counselor.id = :counselorId AND ts.startTime >= :startTime AND ts.endTime <= :endTime")
    List<TimeSlot> findSlotsByTimeRange(@Param("counselorId") Long counselorId, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
    
    // Method for finding available slots by counselor and day (no date since your entity doesn't have it)
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.counselor.id = :counselorId " +
           "AND ts.dayOfWeek = :dayOfWeek " +
           "AND ts.isAvailable = true")
    List<TimeSlot> findAvailableSlotsByCounselorAndDayString(@Param("counselorId") Long counselorId, 
                                                            @Param("dayOfWeek") String dayOfWeek);
    
    // Get all available slots for a specific counselor
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.counselor.id = :counselorId AND ts.isAvailable = true ORDER BY ts.dayOfWeek, ts.startTime")
    List<TimeSlot> findAvailableSlotsByCounselorOrdered(@Param("counselorId") Long counselorId);
    
    // Find slots by time range for a counselor
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.counselor.id = :counselorId " +
           "AND ts.startTime >= :startTime " +
           "AND ts.endTime <= :endTime " +
           "AND ts.isAvailable = true")
    List<TimeSlot> findAvailableSlotsByTimeRange(@Param("counselorId") Long counselorId, 
                                                @Param("startTime") LocalTime startTime, 
                                                @Param("endTime") LocalTime endTime);
}
