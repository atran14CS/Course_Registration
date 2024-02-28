package sba.sms.services;

import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * StudentService is a concrete class. This class implements the
 * StudentI interface, overrides all abstract service methods and
 * provides implementation for each method. Lombok @Log used to
 * generate a logger file.
 */

public class StudentService implements StudentI {
    /**
     *  Given a student object save the student information in the Student table.
     *  if any error occurs during saving process check to see if there is a transaction
     *  in place. If so, rollback the transaction and throw a PersistenceException with error
     *  msg. Finally close session regardless of errors.
     * @param student{object} - Student object
     * @throws PersistenceException
     */
    public void createStudent(Student student) throws PersistenceException {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class).buildSessionFactory();
        Session session = factory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new PersistenceException("Error creating student", e);
        } finally {
            session.close();
        }
    }

    /**
     * Return all students from the Student Table. If any error occurs throw
     * a PersistenceException error with the error msg. Close the session at the
     * end regardless of errors.
     * @return {Object} - list of all students in Student in table
     */
    public List<Student> getAllStudents() {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class).buildSessionFactory();
        Session session = factory.openSession();
        try {
            String query = "FROM Student";
            TypedQuery<Student> allStudents = session.createQuery(query, Student.class);
            List<Student> result = allStudents.getResultList();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error getting all students", e);
        } finally {
            session.close();
        }
    }

    /**
     * Returns the student that has the specify email. If any error occurs throw a PersistenceException
     * with the error msg. Close the session at the end regardless of an error.
     * @param email{String} - String represent a student email
     * @return {Object} Student - the student found by the email
     */
    public Student getStudentByEmail(String email) {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class).buildSessionFactory();
        Session session = factory.openSession();
        try {
            String query = "FROM Student s WHERE s.email = :email";
            TypedQuery<Student> findStudent = session.createQuery(query);
            findStudent.setParameter("email", email);
            Student student = findStudent.getSingleResult();
            return student;
        } catch (Exception e) {
            throw new RuntimeException("Error getting student by email", e);
        } finally {
            session.close();
        }
    }

    /**
     * Finds out if there is a student with the given email and password. Returns true if there is and false if not.
     * Otherwise, throw a PersistenceException with the error msg.
     * @param email{String} - email of student
     * @param password{String} - password of student
     * @return {boolean} - true or false that student exist in Student table.
     */
    public boolean validateStudent(String email, String password) {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class).buildSessionFactory();
        Session session = factory.openSession();
        try {
            String query = "FROM Student s WHERE s.email = :email AND s.password = :password";
            TypedQuery<Student> studentExist = session.createQuery(query, Student.class);
            studentExist.setParameter("email", email);
            studentExist.setParameter("password", password);
            List<Student> student = studentExist.getResultList();
            return !student.isEmpty();
        } catch (Exception e) {
            throw new RuntimeException("Error validating student", e);
        } finally {
            session.close();
        }
    }

    /**
     * Register the student into a course finds the student and course by the given
     * email and courseId if both are found and student is not already register to the course
     * add the course to the student courses and save and commit. Otherwise,
     * rollback the transaction and close session.
     * @param email{String} - email of student
     * @param courseId{int} courseId of courses
     */
    public void registerStudentToCourse(String email, int courseId) {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class).buildSessionFactory();
        Session session = factory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            String findStudentQuery = "FROM Student s WHERE s.email = :email";
            TypedQuery<Student> findStudent = session.createQuery(findStudentQuery, Student.class);
            findStudent.setParameter("email", email);
            Student student = findStudent.getSingleResult();
            String findCourseQuery = "FROM Course c WHERE c.id = :courseId";
            TypedQuery<Course> findCourse = session.createQuery(findCourseQuery, Course.class);
            findCourse.setParameter("courseId", courseId);
            Course course = findCourse.getSingleResult();
            if (student != null && course != null && !student.isEnrolledCourse(course)) {
                student.getCourses().add(course);
                session.persist(student);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new PersistenceException("Error registering student to course", e);
        } finally {
            session.close();
        }
    }

    /**
     * Finds the student courses and returns the courses the student is taking. If any errors a PersistenceException
     * will be thrown with an error msg. Session will close at the end regardless of errors.
     * @param email{String} - email of student
     * @return {Object} - list of courses and the information of the courses (id, name, instructor)
     */
    public List<Course> getStudentCourses(String email) {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Course.class).buildSessionFactory();
        Session session = factory.openSession();
        try {
            String getCourses = "SELECT c.* FROM course c " +
                                "INNER JOIN student_course sc ON c.id = sc.course_id " +
                                "INNER JOIN student s ON s.email = sc.student_email " +
                                "WHERE s.email = :email";
            NativeQuery<Course> query = session.createNativeQuery(getCourses, Course.class);
            query.setParameter("email", email);
            List<Course> courses = query.getResultList();
            return courses;
        } catch (Exception e) {
            throw new RuntimeException("Error getting student courses with details", e);
        } finally {
            session.close();
        }
    }
}
