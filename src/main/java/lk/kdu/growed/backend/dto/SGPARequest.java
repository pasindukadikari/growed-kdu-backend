package lk.kdu.growed.backend.dto;

public class SGPARequest {
    private String indexNumber;
    private String semester;
    private Double sgpa;
    private String calculatedAt;

    // Constructors
    public SGPARequest() {}

    public SGPARequest(String indexNumber, String semester, Double sgpa, String calculatedAt) {
        this.indexNumber = indexNumber;
        this.semester = semester;
        this.sgpa = sgpa;
        this.calculatedAt = calculatedAt;
    }

    // Getters and Setters
    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Double getSgpa() {
        return sgpa;
    }

    public void setSgpa(Double sgpa) {
        this.sgpa = sgpa;
    }

    public String getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(String calculatedAt) {
        this.calculatedAt = calculatedAt;
    }

    @Override
    public String toString() {
        return "SGPARequest{" +
                "indexNumber='" + indexNumber + '\'' +
                ", semester='" + semester + '\'' +
                ", sgpa=" + sgpa +
                ", calculatedAt='" + calculatedAt + '\'' +
                '}';
    }
}
