package lk.kdu.growed.backend.repository;

import lk.kdu.growed.backend.model.CalculatedSGPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculatedSGPARepository extends JpaRepository<CalculatedSGPA, Long> {
    
    CalculatedSGPA findByIndexNumberAndSemester(String indexNumber, String semester);
    
    CalculatedSGPA findFirstByIndexNumberOrderByCalculationDateDesc(String indexNumber);
    
    List<CalculatedSGPA> findByIndexNumberOrderByCalculationDateDesc(String indexNumber);
}
