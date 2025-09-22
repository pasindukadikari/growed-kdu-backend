package lk.kdu.growed.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/counseling")
@CrossOrigin(origins = "*")
public class CounselingController {

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "working");
        response.put("message", "Counseling API is running");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/counselors")
    public ResponseEntity<List<Map<String, Object>>> getAllCounselors() {
        List<Map<String, Object>> counselors = new ArrayList<>();
        
        Map<String, Object> counselor1 = new HashMap<>();
        counselor1.put("id", 1);
        counselor1.put("firstName", "Priyanka");
        counselor1.put("lastName", "Jayawardena");
        counselor1.put("email", "priyanka@kdu.edu");
        counselor1.put("specialization", "Academic Counseling");
        counselor1.put("description", "Specializes in academic stress management, study planning, and career guidance");
        counselor1.put("isActive", true);
        
        Map<String, Object> counselor2 = new HashMap<>();
        counselor2.put("id", 2);
        counselor2.put("firstName", "Kokila");
        counselor2.put("lastName", "Dissanayake");
        counselor2.put("email", "kokila@kdu.edu");
        counselor2.put("specialization", "Academic Counseling");
        counselor2.put("description", "Specializes in academic stress management, study planning, and career guidance");
        counselor2.put("isActive", true);
        
        counselors.add(counselor1);
        counselors.add(counselor2);
        
        return ResponseEntity.ok(counselors);
    }

    @GetMapping("/counselors/{counselorId}/available-slots")
    public ResponseEntity<List<Map<String, Object>>> getAvailableTimeSlots(
            @PathVariable Long counselorId,
            @RequestParam String date) {
        
        List<Map<String, Object>> timeSlots = new ArrayList<>();
        
        Map<String, Object> slot1 = new HashMap<>();
        slot1.put("id", 1);
        slot1.put("startTime", "09:00:00");
        slot1.put("endTime", "10:00:00");
        slot1.put("displayTime", "9:00 AM");
        slot1.put("available", true);
        
        Map<String, Object> slot2 = new HashMap<>();
        slot2.put("id", 2);
        slot2.put("startTime", "11:00:00");
        slot2.put("endTime", "12:00:00");
        slot2.put("displayTime", "11:00 AM");
        slot2.put("available", true);
        
        timeSlots.add(slot1);
        timeSlots.add(slot2);
        
        return ResponseEntity.ok(timeSlots);
    }

    @PostMapping("/appointments")
    public ResponseEntity<Map<String, Object>> bookAppointment(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        System.out.println("Received booking request: " + request);
        
        response.put("success", true);
        response.put("message", "Appointment booked successfully. Waiting for counselor confirmation.");
        response.put("appointmentId", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/resources")
    public ResponseEntity<List<Map<String, Object>>> getAllResources() {
        List<Map<String, Object>> resources = new ArrayList<>();
        
        Map<String, Object> resource1 = new HashMap<>();
        resource1.put("id", 1);
        resource1.put("title", "Core Mindset");
        resource1.put("category", "CORE_MINDSET");
        resource1.put("iconColor", "#FF9A9A");
        
        List<Map<String, Object>> items1 = new ArrayList<>();
        items1.add(createResourceItem(1, "Listen to understand, not to respond", 1));
        items1.add(createResourceItem(2, "Cultivate unconditional positive regard", 2));
        resource1.put("items", items1);
        
        resources.add(resource1);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/resources/{id}")
    public ResponseEntity<Map<String, Object>> getResourceById(@PathVariable Long id) {
        List<Map<String, Object>> allResources = getAllResources().getBody();
        if (allResources != null && !allResources.isEmpty()) {
            return ResponseEntity.ok(allResources.get(0));
        }
        return ResponseEntity.notFound().build();
    }
    
    private Map<String, Object> createResourceItem(int id, String content, int order) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", id);
        item.put("content", content);
        item.put("displayOrder", order);
        return item;
    }
}