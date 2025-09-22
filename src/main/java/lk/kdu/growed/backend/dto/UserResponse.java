



package lk.kdu.growed.backend.dto;

import java.time.LocalDateTime;
import lk.kdu.growed.backend.model.User; // Add this line

public class UserResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String indexNumber;
    private String faculty;
    private String department;
    private LocalDateTime createdAt;
    
    // Default constructor
    public UserResponse() {}
    
    // Constructor from User entity
    public UserResponse(lk.kdu.growed.backend.model.User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.indexNumber = user.getIndexNumber();
        
        this.faculty = user.getFaculty();
        this.department = user.getDepartment();
       
        this.createdAt = user.getCreatedAt();
    }
    
    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getIndexNumber() { return indexNumber; }
    public void setIndexNumber(String indexNumber) { this.indexNumber = indexNumber; }
     
    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; } 
   
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}