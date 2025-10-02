package lk.kdu.growed.backend.dto;

public class AddMarksRequest {
    private String index_number;
    private String subject_code;
    private String semester;
    private double marks;
    private String grade;
    private double gpa;
    private boolean is_repeat;

    // Default constructor
    public AddMarksRequest() {}

    // Getters and Setters
    public String getIndex_number() {
        return index_number;
    }

    public void setIndex_number(String index_number) {
        this.index_number = index_number;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public boolean isIs_repeat() {
        return is_repeat;
    }

    public void setIs_repeat(boolean is_repeat) {
        this.is_repeat = is_repeat;
    }

    @Override
    public String toString() {
        return "AddMarksRequest{" +
                "index_number='" + index_number + '\'' +
                ", subject_code='" + subject_code + '\'' +
                ", semester='" + semester + '\'' +
                ", marks=" + marks +
                ", grade='" + grade + '\'' +
                ", gpa=" + gpa +
                ", is_repeat=" + is_repeat +
                '}';
    }
}
