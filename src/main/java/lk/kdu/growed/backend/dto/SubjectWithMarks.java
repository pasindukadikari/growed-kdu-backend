package lk.kdu.growed.backend.dto;

import java.math.BigDecimal;

public class SubjectWithMarks {
    private String subjectCode;
    private String subjectName;
    private Integer semester;
    private BigDecimal marks;
    private String grade;
    private BigDecimal gpa;
    private String status;
    
    public SubjectWithMarks() {}
    
    public SubjectWithMarks(String subjectCode, String subjectName, Integer semester) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.semester = semester;
        this.status = "pending";
    }
    
    // All getters and setters
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
    
    public BigDecimal getMarks() { return marks; }
    public void setMarks(BigDecimal marks) { this.marks = marks; }
    
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    
    public BigDecimal getGpa() { return gpa; }
    public void setGpa(BigDecimal gpa) { this.gpa = gpa; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}