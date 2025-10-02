package lk.kdu.growed.backend.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lk.kdu.growed.backend.dto.SGPARequest;
import lk.kdu.growed.backend.dto.YGPARequest;
import lk.kdu.growed.backend.model.CalculatedSGPA;
import lk.kdu.growed.backend.model.CalculatedYGPA;
import lk.kdu.growed.backend.repository.CalculatedSGPARepository;
import lk.kdu.growed.backend.repository.CalculatedYGPARepository;
import lk.kdu.growed.backend.repository.StudentMarksRepository;

@RestController
@RequestMapping("/api/academic")
@CrossOrigin(origins = "*")
public class AcademicController {

    @Autowired
    private CalculatedSGPARepository calculatedSGPARepository;
    
    @Autowired
    private CalculatedYGPARepository calculatedYGPARepository;
    
    @Autowired
    private StudentMarksRepository studentMarksRepository;

    // Store calculated SGPA
    @PostMapping("/sgpa")
    public ResponseEntity<Map<String, Object>> storeSGPA(@RequestBody SGPARequest request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("=== STORING SGPA ===");
            System.out.println("Request: " + request.toString());
            
            // Validate input
            if (request.getIndexNumber() == null || request.getIndexNumber().trim().isEmpty()) {
                response.put("success", false);
                response.put("error", "Index number is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (request.getSgpa() == null || request.getSemester() == null) {
                response.put("success", false);
                response.put("error", "SGPA and semester are required");
                return ResponseEntity.badRequest().body(response);
            }

            // Check if record exists for this student and semester
            CalculatedSGPA existingSGPA = calculatedSGPARepository
                .findByIndexNumberAndSemester(request.getIndexNumber(), request.getSemester());

            if (existingSGPA != null) {
                // Update existing record
                existingSGPA.setSgpa(request.getSgpa());
                existingSGPA.setCalculationDate(LocalDateTime.now());
                existingSGPA.setUpdatedAt(LocalDateTime.now());
                calculatedSGPARepository.save(existingSGPA);
                System.out.println("Updated existing SGPA record");
            } else {
                // Create new record
                CalculatedSGPA newSGPA = new CalculatedSGPA();
                newSGPA.setIndexNumber(request.getIndexNumber());
                newSGPA.setSgpa(request.getSgpa());
                newSGPA.setSemester(request.getSemester());
                newSGPA.setCalculationDate(LocalDateTime.now());
                newSGPA.setCreatedAt(LocalDateTime.now());
                calculatedSGPARepository.save(newSGPA);
                System.out.println("Created new SGPA record");
            }

            response.put("success", true);
            response.put("message", "SGPA stored successfully");
            response.put("sgpa", request.getSgpa());
            response.put("semester", request.getSemester());
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error storing SGPA: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "Failed to store SGPA: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Store calculated YGPA
    @PostMapping("/ygpa")
    public ResponseEntity<Map<String, Object>> storeYGPA(@RequestBody YGPARequest request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("=== STORING YGPA ===");
            System.out.println("Request: " + request.toString());
            
            // Validate input
            if (request.getIndexNumber() == null || request.getIndexNumber().trim().isEmpty()) {
                response.put("success", false);
                response.put("error", "Index number is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (request.getYgpa() == null || request.getAcademicYear() == null) {
                response.put("success", false);
                response.put("error", "YGPA and academic year are required");
                return ResponseEntity.badRequest().body(response);
            }

            // Check if record exists for this student and academic year
            CalculatedYGPA existingYGPA = calculatedYGPARepository
                .findByIndexNumberAndAcademicYear(request.getIndexNumber(), request.getAcademicYear());

            if (existingYGPA != null) {
                // Update existing record
                existingYGPA.setYgpa(request.getYgpa());
                existingYGPA.setCalculationDate(LocalDateTime.now());
                existingYGPA.setUpdatedAt(LocalDateTime.now());
                calculatedYGPARepository.save(existingYGPA);
                System.out.println("Updated existing YGPA record");
            } else {
                // Create new record
                CalculatedYGPA newYGPA = new CalculatedYGPA();
                newYGPA.setIndexNumber(request.getIndexNumber());
                newYGPA.setYgpa(request.getYgpa());
                newYGPA.setAcademicYear(request.getAcademicYear());
                newYGPA.setCalculationDate(LocalDateTime.now());
                newYGPA.setCreatedAt(LocalDateTime.now());
                calculatedYGPARepository.save(newYGPA);
                System.out.println("Created new YGPA record");
            }

            response.put("success", true);
            response.put("message", "YGPA stored successfully");
            response.put("ygpa", request.getYgpa());
            response.put("academic_year", request.getAcademicYear());
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error storing YGPA: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "Failed to store YGPA: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

 // In AcademicController.java, update the getAcademicSummary method:

@GetMapping("/summary")
public ResponseEntity<Map<String, Object>> getAcademicSummary(@RequestParam String indexNumber) {
    Map<String, Object> response = new HashMap<>();
    
    try {
        System.out.println("=== GETTING ACADEMIC SUMMARY ===");
        System.out.println("Index Number: " + indexNumber);
        
        // Get latest SGPA
        CalculatedSGPA latestSGPA = calculatedSGPARepository
            .findFirstByIndexNumberOrderByCalculationDateDesc(indexNumber);
        
        // Get latest YGPA
        CalculatedYGPA latestYGPA = calculatedYGPARepository
            .findFirstByIndexNumberOrderByCalculationDateDesc(indexNumber);
        
        // Get current semester from marks
        Integer currentSemester = getCurrentSemesterFromMarks(indexNumber);
        
        // Build response
        response.put("currentSGPA", latestSGPA != null ? latestSGPA.getSgpa() : 0.0);
        response.put("overallYGPA", latestYGPA != null ? latestYGPA.getYgpa() : 0.0);
        response.put("currentSemester", currentSemester != null ? currentSemester : 1);
        response.put("messageCount", 12);
        response.put("hasNewMessages", true);
        response.put("hasCalculatedValues", 
            (latestSGPA != null && latestSGPA.getSgpa() > 0) || 
            (latestYGPA != null && latestYGPA.getYgpa() > 0));
        
        // IMPORTANT: Changed from lastSGPASemester to semesterCalculated
        if (latestSGPA != null) {
            response.put("semesterCalculated", latestSGPA.getSemester());  // Changed field name
            response.put("lastSGPAUpdate", latestSGPA.getCalculationDate());
        } else {
            response.put("semesterCalculated", null);
        }
        
        if (latestYGPA != null) {
            response.put("lastYGPAUpdate", latestYGPA.getCalculationDate());
            response.put("lastYGPAYear", latestYGPA.getAcademicYear());
        }
        
        System.out.println("Academic summary response: " + response);
        return ResponseEntity.ok(response);

    } catch (Exception e) {
        System.err.println("Error getting academic summary: " + e.getMessage());
        e.printStackTrace();
        response.put("error", "Failed to get academic summary: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
    // Get SGPA history
    @GetMapping("/sgpa-history")
    public ResponseEntity<Map<String, Object>> getSGPAHistory(@RequestParam String indexNumber) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<CalculatedSGPA> sgpaHistory = calculatedSGPARepository
                .findByIndexNumberOrderByCalculationDateDesc(indexNumber);
            
            response.put("sgpaHistory", sgpaHistory);
            response.put("totalRecords", sgpaHistory.size());
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error getting SGPA history: " + e.getMessage());
            response.put("error", "Failed to get SGPA history: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get YGPA history
    @GetMapping("/ygpa-history")
    public ResponseEntity<Map<String, Object>> getYGPAHistory(@RequestParam String indexNumber) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<CalculatedYGPA> ygpaHistory = calculatedYGPARepository
                .findByIndexNumberOrderByCalculationDateDesc(indexNumber);
            
            response.put("ygpaHistory", ygpaHistory);
            response.put("totalRecords", ygpaHistory.size());
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error getting YGPA history: " + e.getMessage());
            response.put("error", "Failed to get YGPA history: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Helper method to get current semester from marks
    // In AcademicController.java, replace getCurrentSemesterFromMarks method:

private Integer getCurrentSemesterFromMarks(String indexNumber) {
    try {
        // Get the latest SGPA to determine current semester
        CalculatedSGPA latestSGPA = calculatedSGPARepository
            .findFirstByIndexNumberOrderByCalculationDateDesc(indexNumber);
        
        if (latestSGPA != null && latestSGPA.getSemester() != null) {
            // Extract semester number from string like "1st Semester", "2nd Semester", etc.
            String semester = latestSGPA.getSemester();
            String number = semester.replaceAll("[^0-9]", ""); // Extract just the number
            if (!number.isEmpty()) {
                return Integer.parseInt(number);
            }
        }
        
        return 1; // Default to 1 if no SGPA found
    } catch (Exception e) {
        System.err.println("Error getting current semester: " + e.getMessage());
        return 1; // Default
    }
}

    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Academic controller is working!");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}
