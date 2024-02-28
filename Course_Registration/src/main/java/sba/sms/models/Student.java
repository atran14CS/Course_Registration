package sba.sms.models;

import jakarta.persistence.*;
import java.util.*;


/**
 * Student is a POJO, configured as a persistent class that represents (or maps to) a table
 * name 'student' in the database. A Student object contains fields that represent student
 * login credentials and a join table containing a registered student's email and course(s)
 * data. The Student class can be viewed as the owner of the bi-directional relationship.
 * Implement Lombok annotations to eliminate boilerplate code.
 */

@Entity
@Table
public class Student {
    @Id
    private String email;
    private String name;
    private String password;
    @ManyToMany(targetEntity = Course.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable (
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_email", referencedColumnName = "email"),
        inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"))
    private Set<Course> courses = new HashSet<>();

    // no args constructor
    public Student() {
    }

    // all args constructor
    public Student(String email, String name, String password, Set<Course> courses) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.courses = courses;
    }

    // required args constructor
    public Student(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    // getters and setters for fields
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    // Helper method checks to see if student is enrolled in the course;
    public boolean isEnrolledCourse(Course course) {
        return courses.contains(course);
    }

    // Override toString method returns the string format of student object
    @Override
    public String toString() {
        return "Student[email: " + email + ", name: " + name + ", password: " + password +"]";
    }

    // Override hashCode method returns hashCode of object
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + (((courses == null)) ? 0 : courses.hashCode());
        return result;
    }

    // Override equals method compares the other student object and returns true or false
    @Override
    public boolean equals(Object o) {
        if (this.toString().equals((o.toString()))) {
            return true;
        }
        if (o instanceof Student) {
            Student other = (Student) o;
            boolean sameEmail = (this.email.equals(other.getEmail()));
            boolean sameName = (this.name.equals(other.getName()));
            boolean password = (this.password.equals(other.getPassword()));
            boolean courses = (this.courses.equals(other.getCourses()));
            if(sameEmail && sameName && password && courses) {
                return true;
            }
        }
        return false;
    }
}



