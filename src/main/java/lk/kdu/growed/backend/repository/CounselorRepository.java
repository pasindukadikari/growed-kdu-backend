package lk.kdu.growed.backend.repository;

import lk.kdu.growed.backend.model.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long> {
    
    Optional<Counselor> findByEmail(String email);
    
    List<Counselor> findByIsActiveTrue();
    
    List<Counselor> findBySpecializationContainingIgnoreCase(String specialization);
    
    @Query("SELECT c FROM Counselor c WHERE c.isActive = true AND " +
           "(c.firstName LIKE %?1% OR c.lastName LIKE %?1% OR c.specialization LIKE %?1%)")
    List<Counselor> searchCounselors(String keyword);

    List<Counselor> findByIsActiveTrueOrderByFirstNameAsc();
}