package facades;

import dto.CourseDTO;
import entities.Course;
import errorhandling.API_Exception;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseFacadeTest {

    private static EntityManagerFactory emf;
    private static CourseFacade facade;
    private static Course c1, c2;

    public CourseFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = CourseFacade.getCourseFacade(emf);
        EntityManager em = emf.createEntityManager();

        c1 = new Course("security","beginner");
        c2 = new Course("javascript","advanced");
        try {
            em.getTransaction().begin();
            em.createQuery("delete from Course").executeUpdate();
            em.persist(c1);
            em.persist(c2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterAll
    public static void tearDownClass() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("delete from Course").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testAddCourseFromFacade() throws API_Exception {
        //Arrange
        Course course = new Course("security", "beginner");
        CourseDTO dto = new CourseDTO(course);

        //Act
        CourseDTO newCourse = facade.addCourse(dto);

        //Assert
        assertEquals("security", newCourse.getCourseName());
    }

    @Test
    public void testAddCourseFromFacadeException() {
        //Arrange
        Course course = new Course("security", null);
        CourseDTO dto = new CourseDTO(course);

        try{
        //Act
            CourseDTO newCourse = facade.addCourse(dto);
        } catch(API_Exception ex){
        //Assert
            assertEquals(400, ex.getErrorCode());
        }
    }

    @Test
    public void getAllCoursesFacade() throws API_Exception {
        //Act
        CourseDTO[] courses = facade.getAllCourses();

        //Assert
        assertEquals(2, courses.length);
    }

}
