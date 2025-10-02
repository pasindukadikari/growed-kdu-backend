package lk.kdu.growed.backend.service;

import lk.kdu.growed.backend.model.AdminUser;
import lk.kdu.growed.backend.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Map<String, Object> authenticateAdmin(String username, String password) {
        try {
            System.out.println("Admin login attempt for username: " + username);
            
            Optional<AdminUser> adminOptional = adminUserRepository.findByUsername(username);
            
            if (adminOptional.isEmpty()) {
                System.out.println("Admin not found: " + username);
                throw new RuntimeException("Invalid credentials");
            }

            AdminUser admin = adminOptional.get();
            
            if (!admin.getIsActive()) {
                System.out.println("Admin account disabled: " + username);
                throw new RuntimeException("Account is disabled");
            }

            if (!passwordEncoder.matches(password, admin.getPassword())) {
                System.out.println("Invalid password for admin: " + username);
                throw new RuntimeException("Invalid credentials");
            }

            System.out.println("Admin login successful: " + username);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("admin", convertAdminToMap(admin));
            response.put("token", "admin-token-" + admin.getId());

            return response;
        } catch (Exception e) {
            System.err.println("Admin authentication error: " + e.getMessage());
            throw e;
        }
    }

    private Map<String, Object> convertAdminToMap(AdminUser admin) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", admin.getId());
        map.put("username", admin.getUsername());
        map.put("fullName", admin.getFullName());
        map.put("email", admin.getEmail());
        map.put("role", admin.getRole().toString());
        return map;
    }
}
