package sba.sms.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Course is a POJO, configured as a persistent class that represents (or maps to) a table
 * name 'course' in the database. A Course object contains fields that represent course
 * information and a mapping of 'courses' that indicate an inverse or referencing side
 * of the relationship. Implement Lombok annotations to eliminate boilerplate code.
 */

@Entity
@Table
public class Course {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY )
    private int id;
    private String name;
    private String instructor;
    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Student> students = new HashSet<>();

    // no args constructor
    public Course() {}

    // all args constructor
    public Course(String name, String instructor, Set<Student> students) {
        this.name = name;
        this.instructor = instructor;
        this.students = students;
    }

    // require arg constructor
    public Course(String name, String instructor) {
        this.name = name;
        this.instructor = instructor;
    }

    // getter and setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    // Helper method checks to see if student is enrolled in course
    public boolean containsStudent(Student student) {
        return students.contains(student);
    }

    // Override toString method
    @Override
    public String toString() {
        return "Course[id: " + id + ", name: " + name + ", instructor: " + instructor + "]";
    }

    // Override hashCode method
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((instructor == null) ? 0 : instructor.hashCode());
        return result;
    }

    // Override equals method
    @Override
    public boolean equals(Object o) {
        if (this.toString().equals(o.toString())) {
            return true;
        }
        if (o instanceof Course) {
            Course other = (Course) o;
            boolean sameId = (this.id == other.getId());
            boolean sameName = (this.name.equals(other.getName()));
            boolean sameInstructor = (this.instructor.equals(other.getInstructor()));
            boolean sameStudents = (this.students.equals(other.getStudents()));
            if(sameId && sameName && sameInstructor && sameStudents) {
                return true;
            }
        }
        return false;
    }
}
