package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "class")
public class Class implements Serializable{

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    private static final long serialVersionUID = 1L;
    @NotNull
    @Column(name = "semester")
    private String semester;
    @NotNull
    @Column(name = "numberOfStudents")
    private int numberOfStudents;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Course course;

    public Class(@NotNull String semester, @NotNull int numberOfStudents, Course course) {
        this.semester = semester;
        this.numberOfStudents = numberOfStudents;
        this.course = course;
    }

    public Class() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
