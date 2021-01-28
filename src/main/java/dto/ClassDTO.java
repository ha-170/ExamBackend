package dto;

import entities.Class;

public class ClassDTO {
    private Long id;
    private String semester;
    private int numberOfStudents;
    private CourseDTO course;

    public ClassDTO(Class c) {
        this.id = c.getId();
        this.semester = c.getSemester();
        this.numberOfStudents = c.getNumberOfStudents();
        this.course = new CourseDTO(c.getCourse());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }
}
