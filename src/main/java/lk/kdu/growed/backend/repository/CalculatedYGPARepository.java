package lk.kdu.growed.backend.repository;

import lk.kdu.growed.backend.model.CalculatedYGPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculatedYGPARepository extends JpaRepository<CalculatedYGPA, Long> {
    
    CalculatedYGPA findByIndexNumberAndAcademicYear(String indexNumber, String academicYear);
    
    CalculatedYGPA findFirstByIndexNumberOrderByCalculationDateDesc(String indexNumber);
    
    List<CalculatedYGPA> findByIndexNumberOrderByCalculationDateDesc(String indexNumber);
}
