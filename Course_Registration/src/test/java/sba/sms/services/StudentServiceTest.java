package sba.sms.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sba.sms.models.Student;
import sba.sms.utils.CommandLine;
import jakarta.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;


class StudentServiceTest {
    Student student1;
    Student student2;
    Student student3;
    StudentService sService;

    @BeforeEach
    // Sets up the students that will be tested
    public void setUp() throws Exception {
        student1 = new Student("reema@gmail.com", "reema brown", "password");
        student2 = new Student("akira@gmail.com", "akira tran", "password");
        student3 = new Student("annette@gmail.com", "annette allen", "password");
        sService = new StudentService();
    }

    @Test
    // Test if student exist in the Student table
    public void testValidateStudent() {
        assertEquals(true, sService.validateStudent(student1.getEmail(), student1.getPassword()));
        assertEquals(false, sService.validateStudent(student2.getEmail(), student2.getPassword()));
        assertEquals(true, sService.validateStudent(student3.getEmail(), student3.getPassword()));
    }
}