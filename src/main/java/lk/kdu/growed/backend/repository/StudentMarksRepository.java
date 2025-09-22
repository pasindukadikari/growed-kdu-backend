package lk.kdu.growed.backend.repository;

import lk.kdu.growed.backend.model.StudentMarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentMarksRepository extends JpaRepository<StudentMarks, Long> {
    
    // Find marks by student index number
    List<StudentMarks> findByIndexNumber(String indexNumber);
    
    // Find marks by index number and semester
    List<StudentMarks> findByIndexNumberAndSemester(String indexNumber, String semester);
    
    // Find marks by index number and subject code
    StudentMarks findByIndexNumberAndSubjectCode(String indexNumber, String subjectCode);
}