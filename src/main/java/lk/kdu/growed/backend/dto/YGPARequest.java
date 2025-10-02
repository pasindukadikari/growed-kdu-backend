package lk.kdu.growed.backend.dto;

public class YGPARequest {
    private String indexNumber;
    private String academicYear;
    private Double ygpa;
    private String calculatedAt;

    // Constructors
    public YGPARequest() {}

    public YGPARequest(String indexNumber, String academicYear, Double ygpa, String calculatedAt) {
        this.indexNumber = indexNumber;
        this.academicYear = academicYear;
        this.ygpa = ygpa;
        this.calculatedAt = calculatedAt;
    }

    // Getters and Setters
    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public Double getYgpa() {
        return ygpa;
    }

    public void setYgpa(Double ygpa) {
        this.ygpa = ygpa;
    }

    public String getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(String calculatedAt) {
        this.calculatedAt = calculatedAt;
    }

    @Override
    public String toString() {
        return "YGPARequest{" +
                "indexNumber='" + indexNumber + '\'' +
                ", academicYear='" + academicYear + '\'' +
                ", ygpa=" + ygpa +
                ", calculatedAt='" + calculatedAt + '\'' +
                '}';
    }
}
