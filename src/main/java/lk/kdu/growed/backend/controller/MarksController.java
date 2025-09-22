package lk.kdu.growed.backend.controller;

import lk.kdu.growed.backend.model.StudentMarks;
import lk.kdu.growed.backend.model.User;
import lk.kdu.growed.backend.repository.StudentMarksRepository;
import lk.kdu.growed.backend.repository.UserRepository;
import lk.kdu.growed.backend.dto.AddMarksRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MarksController {

    @Autowired
    private StudentMarksRepository studentMarksRepository;

    @Autowired
    private UserRepository userRepository;

    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Marks API is working!");
    }

    // Test POST endpoint
    @PostMapping("/test-request")
    public ResponseEntity<Map<String, Object>> testRequest(@RequestBody Map<String, Object> request) {
        System.out.println("Raw request received: " + request);
        Map<String, Object> response = new HashMap<>();
        response.put("received", request);
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    // GET marks for a specific student using query parameter
    @GetMapping("/marks")
    public ResponseEntity<?> getMarksByIndexNumber(@RequestParam String indexNumber) {
        try {
            System.out.println("=== GET MARKS DEBUG ===");
            System.out.println("Getting marks for index number: " + indexNumber);
            
            List<StudentMarks> marks = studentMarksRepository.findByIndexNumber(indexNumber);
            System.out.println("Found " + marks.size() + " marks records for: " + indexNumber);
            
            // Debug the marks found
            for (StudentMarks mark : marks) {
                System.out.println("Mark: " + mark.getSubjectCode() + " - " + mark.getMarks() + " - " + mark.getSemester());
            }
            
            return ResponseEntity.ok(marks);
        } catch (Exception e) {
            System.err.println("Error getting marks: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error getting marks: " + e.getMessage());
        }
    }

    // Alternative GET method using path variable (keep for backward compatibility if needed)
    @GetMapping("/marks/{indexNumber}")
    public ResponseEntity<?> getMarksByIndexNumberPath(@PathVariable String indexNumber) {
        try {
            // Decode the URL-encoded index number
            String decodedIndexNumber = java.net.URLDecoder.decode(indexNumber, "UTF-8");
            System.out.println("Received encoded index number: " + indexNumber);
            System.out.println("Decoded index number: " + decodedIndexNumber);
            
            List<StudentMarks> marks = studentMarksRepository.findByIndexNumber(decodedIndexNumber);
            System.out.println("Found " + marks.size() + " marks for index: " + decodedIndexNumber);
            
            return ResponseEntity.ok(marks);
        } catch (Exception e) {
            System.err.println("Error getting marks: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error getting marks: " + e.getMessage());
        }
    }

    // POST - Add new marks
    @PostMapping("/marks")
    public ResponseEntity<?> addMarks(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("=== MARKS CONTROLLER DEBUG ===");
            System.out.println("Received raw request: " + request);
            
            // Extract values from the map
            String indexNumber = (String) request.get("index_number");
            String subjectCode = (String) request.get("subject_code");
            String semester = (String) request.get("semester");
            Object marksObj = request.get("marks");
            String grade = (String) request.get("grade");
            Object gpaObj = request.get("gpa");
            Object isRepeatObj = request.get("is_repeat");
            
            System.out.println("Extracted values:");
            System.out.println("Index Number: " + indexNumber);
            System.out.println("Subject Code: " + subjectCode);
            System.out.println("Semester: " + semester);
            System.out.println("Marks: " + marksObj);
            System.out.println("Grade: " + grade);
            System.out.println("GPA: " + gpaObj);
            System.out.println("Is Repeat: " + isRepeatObj);

            // Validate required fields
            if (indexNumber == null || indexNumber.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Index number is required");
            }
            
            if (subjectCode == null || subjectCode.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Subject code is required");
            }

            // Convert values
            double marks = 0.0;
            double gpa = 0.0;
            boolean isRepeat = false;
            
            if (marksObj instanceof Number) {
                marks = ((Number) marksObj).doubleValue();
            }
            if (gpaObj instanceof Number) {
                gpa = ((Number) gpaObj).doubleValue();
            }
            if (isRepeatObj instanceof Boolean) {
                isRepeat = (Boolean) isRepeatObj;
            }

            // Get student email from User table using index number
            String studentEmail = null;
            try {
                Optional<User> userOpt = userRepository.findByIndexNumber(indexNumber);
                if (userOpt.isPresent()) {
                    studentEmail = userOpt.get().getEmail();
                    System.out.println("Found student email: " + studentEmail);
                } else {
                    System.out.println("Warning: No user found for index number: " + indexNumber);
                    studentEmail = "unknown@example.com"; // Fallback value
                }
            } catch (Exception e) {
                System.err.println("Error finding user: " + e.getMessage());
                studentEmail = "unknown@example.com"; // Fallback value
            }

            // Create and save StudentMarks
            StudentMarks studentMarks = new StudentMarks();
            studentMarks.setIndexNumber(indexNumber);
            studentMarks.setStudentEmail(studentEmail); // Set the email
            studentMarks.setSubjectCode(subjectCode);
            studentMarks.setSemester(semester);
            studentMarks.setMarks(marks);
            studentMarks.setGrade(grade);
            studentMarks.setGpa(gpa);
            studentMarks.setIsRepeat(isRepeat);

            System.out.println("About to save: " + studentMarks);
            StudentMarks savedMarks = studentMarksRepository.save(studentMarks);
            System.out.println("✅ Marks saved successfully: " + savedMarks);
            
            return ResponseEntity.ok(savedMarks);
            
        } catch (Exception e) {
            System.err.println("❌ Error saving marks: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Additional utility endpoint - Delete marks by ID (optional)
    @DeleteMapping("/marks/{id}")
    public ResponseEntity<?> deleteMarks(@PathVariable Long id) {
        try {
            System.out.println("Deleting marks with ID: " + id);
            studentMarksRepository.deleteById(id);
            System.out.println("✅ Marks deleted successfully");
            return ResponseEntity.ok("Marks deleted successfully");
        } catch (Exception e) {
            System.err.println("❌ Error deleting marks: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error deleting marks: " + e.getMessage());
        }
    }

    // Additional utility endpoint - Get all marks (for admin/debugging)
    @GetMapping("/marks/all")
    public ResponseEntity<?> getAllMarks() {
        try {
            System.out.println("Getting all marks from database");
            List<StudentMarks> allMarks = studentMarksRepository.findAll();
            System.out.println("Found " + allMarks.size() + " total marks records");
            return ResponseEntity.ok(allMarks);
        } catch (Exception e) {
            System.err.println("Error getting all marks: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error getting all marks: " + e.getMessage());
        }
    }
}