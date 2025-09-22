package lk.kdu.growed.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student_marks")
public class StudentMarks {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "index_number")
    private String indexNumber;
    
    @Column(name = "student_email") // Add this field
    private String studentEmail;
    
    @Column(name = "subject_code")
    private String subjectCode;
    
    @Column(name = "semester")
    private String semester;
    
    @Column(name = "marks")
    private double marks;
    
    @Column(name = "grade")
    private String grade;
    
    @Column(name = "gpa")
    private double gpa;
    
    @Column(name = "is_repeat")
    private boolean isRepeat;

    // Default constructor
    public StudentMarks() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIndexNumber() { return indexNumber; }
    public void setIndexNumber(String indexNumber) { this.indexNumber = indexNumber; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public boolean getIsRepeat() { return isRepeat; }
    public void setIsRepeat(boolean isRepeat) { this.isRepeat = isRepeat; }

    @Override
    public String toString() {
        return "StudentMarks{" +
                "id=" + id +
                ", indexNumber='" + indexNumber + '\'' +
                ", studentEmail='" + studentEmail + '\'' +
                ", subjectCode='" + subjectCode + '\'' +
                ", semester='" + semester + '\'' +
                ", marks=" + marks +
                ", grade='" + grade + '\'' +
                ", gpa=" + gpa +
                ", isRepeat=" + isRepeat +
                '}';
    }
}