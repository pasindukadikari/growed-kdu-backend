package lk.kdu.growed.backend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import lk.kdu.growed.backend.dto.LoginRequest;
import lk.kdu.growed.backend.dto.RegisterRequest;
import lk.kdu.growed.backend.model.User;
import lk.kdu.growed.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- MOBILE LOGIN ---
    @PostMapping("/mobile/login")
    public ResponseEntity<Map<String, Object>> mobileLogin(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("=== MOBILE LOGIN REQUEST ===");
            System.out.println("Mobile login attempt for email: " + loginRequest.getEmail());
            
            // Validate input
            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Password is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Find user by email
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
            if (userOptional.isEmpty()) {
                System.out.println("User not found: " + loginRequest.getEmail());
                response.put("success", false);
                response.put("message", "Invalid email or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            User user = userOptional.get();
            
            // Verify password
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                System.out.println("Mobile login successful for: " + user.getEmail());
                
                // Create user data for response
                Map<String, Object> userData = new HashMap<>();
                userData.put("email", user.getEmail());
                userData.put("firstName", user.getFirstName());
                userData.put("lastName", user.getLastName());
                userData.put("indexNumber", user.getIndexNumber());
                userData.put("phone", user.getPhone());
                userData.put("faculty", user.getFaculty());
                userData.put("department", user.getDepartment());
                userData.put("role", "STUDENT");
                
                // Successful response
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("user", userData);
                response.put("accessToken", "dummy-token-" + user.getEmail().hashCode()); // Add real JWT later
                response.put("refreshToken", "dummy-refresh-" + user.getEmail().hashCode()); // Add real JWT later
                
                return ResponseEntity.ok(response);
            }
            
            System.out.println("Invalid password for: " + loginRequest.getEmail());
            response.put("success", false);
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            
        } catch (Exception e) {
            System.err.println("Mobile login error: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // --- MOBILE REGISTER ---
    @PostMapping("/mobile/register")
    public ResponseEntity<Map<String, Object>> mobileRegister(@RequestBody RegisterRequest registerRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("=== MOBILE REGISTRATION REQUEST ===");
            System.out.println("Received registration request: " + registerRequest.toString());
            
            // Validate required fields
            if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email is required");
                return ResponseEntity.badRequest().body(response);
            }
            if (registerRequest.getIndexNumber() == null || registerRequest.getIndexNumber().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Index Number is required");
                return ResponseEntity.badRequest().body(response);
            }
            if (registerRequest.getFirstName() == null || registerRequest.getFirstName().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "First name is required");
                return ResponseEntity.badRequest().body(response);
            }
            if (registerRequest.getLastName() == null || registerRequest.getLastName().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Last name is required");
                return ResponseEntity.badRequest().body(response);
            }
            if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Password is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Check if email already exists
            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                System.out.println("Email already exists: " + registerRequest.getEmail());
                response.put("success", false);
                response.put("message", "Email already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            // Check if index number already exists
            if (userRepository.existsByIndexNumber(registerRequest.getIndexNumber())) {
                System.out.println("Index Number already exists: " + registerRequest.getIndexNumber());
                response.put("success", false);
                response.put("message", "Index Number already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            // Create new user
            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setPhone(registerRequest.getPhone());
            user.setIndexNumber(registerRequest.getIndexNumber());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setFaculty(registerRequest.getFaculty());
            user.setDepartment(registerRequest.getDepartment());
            
            System.out.println("Saving user to database...");
            
            // Save user
            User savedUser = userRepository.save(user);
            
            // Create user data for response
            Map<String, Object> userData = new HashMap<>();
            userData.put("email", savedUser.getEmail());
            userData.put("firstName", savedUser.getFirstName());
            userData.put("lastName", savedUser.getLastName());
            userData.put("indexNumber", savedUser.getIndexNumber());
            userData.put("phone", savedUser.getPhone());
            userData.put("faculty", savedUser.getFaculty());
            userData.put("department", savedUser.getDepartment());
            userData.put("role", "STUDENT");
            
            response.put("success", true);
            response.put("message", "Registration successful");
            response.put("user", userData);
            
            System.out.println("=== REGISTRATION SUCCESS ===");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            System.err.println("=== REGISTRATION ERROR ===");
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // --- WEB LOGIN (for counselors/admin) ---
    @PostMapping("/web/login")
    public ResponseEntity<Map<String, Object>> webLogin(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("=== WEB LOGIN REQUEST ===");
            System.out.println("Web login attempt for email: " + loginRequest.getEmail());
            
            // Implementation similar to mobile login but for web users
            response.put("success", false);
            response.put("message", "Web login not implemented yet");
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // --- LEGACY ENDPOINTS (keep for backward compatibility) ---
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            System.out.println("=== LEGACY REGISTRATION REQUEST ===");
            System.out.println("Received registration request: " + registerRequest.toString());
            
            // Validate required fields
            if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (registerRequest.getIndexNumber() == null || registerRequest.getIndexNumber().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Index Number is required");
            }
            if (registerRequest.getFirstName() == null || registerRequest.getFirstName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("First name is required");
            }
            if (registerRequest.getLastName() == null || registerRequest.getLastName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Last name is required");
            }
            if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }
            
            // Check if email already exists
            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                System.out.println("Email already exists: " + registerRequest.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }
            
            // Check if index number already exists
            if (userRepository.existsByIndexNumber(registerRequest.getIndexNumber())) {
                System.out.println("Index Number already exists: " + registerRequest.getIndexNumber());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Index Number already exists");
            }
            
            // Create user
            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setPhone(registerRequest.getPhone());
            user.setIndexNumber(registerRequest.getIndexNumber());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setFaculty(registerRequest.getFaculty());
            user.setDepartment(registerRequest.getDepartment());
            
            System.out.println("Saving user to database...");
            
            // Save user
            User savedUser = userRepository.save(user);
            
            // Create clean response object
            User responseUser = createUserResponse(savedUser);
            
            System.out.println("=== REGISTRATION SUCCESS ===");
            return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
            
        } catch (Exception e) {
            System.err.println("=== REGISTRATION ERROR ===");
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("=== LEGACY LOGIN REQUEST ===");
            System.out.println("Login attempt for email: " + loginRequest.getEmail());
            
            // Validate input
            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }
            
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
            if (userOptional.isEmpty()) {
                System.out.println("User not found: " + loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            User user = userOptional.get();
            
            // Use password encoder to check password
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                System.out.println("Login successful for: " + user.getEmail());
                
                // Create clean response object
                User responseUser = createUserResponse(user);
                return ResponseEntity.ok(responseUser);
            }
            
            System.out.println("Invalid password for: " + loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Login failed: " + e.getMessage());
        }
    }

    // --- HELPER METHODS ---
    private User createUserResponse(User user) {
        User responseUser = new User();
        responseUser.setEmail(user.getEmail());
        responseUser.setFirstName(user.getFirstName());
        responseUser.setLastName(user.getLastName());
        responseUser.setIndexNumber(user.getIndexNumber());
        responseUser.setPhone(user.getPhone());
        responseUser.setFaculty(user.getFaculty());
        responseUser.setDepartment(user.getDepartment());
        responseUser.setCreatedAt(user.getCreatedAt());
        // Explicitly don't set password
        return responseUser;
    }
    
    // --- TEST ENDPOINTS ---
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Auth controller is working!");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/test")
    public ResponseEntity<Map<String, Object>> testPost() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "POST test working!");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}
