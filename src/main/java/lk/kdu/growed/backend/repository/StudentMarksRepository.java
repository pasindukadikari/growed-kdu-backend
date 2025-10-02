package lk.kdu.growed.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import lk.kdu.growed.backend.model.StudentMarks;

@Repository
public interface StudentMarksRepository extends JpaRepository<StudentMarks, Long> {
    
    // Find marks by index number
    List<StudentMarks> findByIndexNumber(String indexNumber);
    
    // Find marks by index number and semester
    List<StudentMarks> findByIndexNumberAndSemester(String indexNumber, String semester);
    
    // Find marks by index number where marks > 0
    List<StudentMarks> findByIndexNumberAndMarksGreaterThan(String indexNumber, Double marks);
    
    // Custom query to get current semester for a student
    @Query("SELECT MAX(CASE " +
           "WHEN s.semester = '1st Semester' THEN 1 " +
           "WHEN s.semester = '2nd Semester' THEN 2 " +
           "WHEN s.semester = '3rd Semester' THEN 3 " +
           "WHEN s.semester = '4th Semester' THEN 4 " +
           "WHEN s.semester = '5th Semester' THEN 5 " +
           "WHEN s.semester = '6th Semester' THEN 6 " +
           "WHEN s.semester = '7th Semester' THEN 7 " +
           "WHEN s.semester = '8th Semester' THEN 8 " +
           "ELSE 1 END) " +
           "FROM StudentMarks s " +
           "WHERE s.indexNumber = :indexNumber AND s.marks > 0")
    Integer getCurrentSemesterByIndexNumber(@Param("indexNumber") String indexNumber);
}
