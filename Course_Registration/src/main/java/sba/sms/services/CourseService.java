package sba.sms.services;

import jakarta.persistence.PersistenceException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * CourseService is a concrete class. This class implements the
 * CourseI interface, overrides all abstract service methods and
 * provides implementation for each method.
 */
public class CourseService implements CourseI {

    /**
     * Creates courses for Students to take and saves the course into the Course table. If the transaction
     * is in play roll back the transaction to prevent any unwanted operations. Also throw a persistence
     * error with the error msg and close the session the end regardless of errors.
     * @param course {object} - course object containing the course information
     */
    public void createCourse(Course course) {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Course.class).buildSessionFactory();
        Session session = factory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(course);
            transaction.commit();
        }catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            throw new PersistenceException("Error creating course", e);
        } finally {
            session.close();
        }
    }

    /**
     * Gets all the created course from the Course Table. If any errors occur a PersistenceException
     * will be thrown with an error msg. Close session at the end regardless of errors.
     * @return {object} a list of the courses offered
     */
    public List<Course> getAllCourses() {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Course.class).buildSessionFactory();
        Session session = factory.openSession();
        try {
            String query = "FROM Course";
            Query<Course> allCourses = session.createQuery(query, Course.class);
            List<Course> result = allCourses.getResultList();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error getting all courses", e);
        } finally {
            session.close();
        }
    }

    /**
     * Finds the course where the course id equals the given courseId. if any errors occur a
     * PersistenceException will be thrown with an error msg. Close session at the end regardless
     * of errors.
     * @param courseId {int} - the id of the course
     * @return {object} the course that has the matching courseId
     */
    public Course getCourseById(int courseId) {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Course.class).buildSessionFactory();
        Session session = factory.openSession();

        try {
            String query = "FROM Course c WHERE c.id = :courseId";
            Query<Course> courseExist = session.createQuery(query, Course.class);
            courseExist.setParameter("courseId", courseId);
            Course course = courseExist.getSingleResult();
            return course;
        } catch (Exception e) {
            throw new RuntimeException("Error finding course by id", e);
        } finally {
            session.close();
        }
    }
}
