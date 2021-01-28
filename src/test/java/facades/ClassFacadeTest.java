package facades;

import dto.ClassDTO;
import entities.Class;
import entities.Course;
import errorhandling.API_Exception;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClassFacadeTest {

    private static EntityManagerFactory emf;
    private static ClassFacade facade;
    private static Course c1, c2;
    private static Class cl1, cl2;

    public ClassFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = ClassFacade.getClassFacade(emf);
        EntityManager em = emf.createEntityManager();

        c1 = new Course("security","beginner");
        c2 = new Course("javascript","advanced");

        cl1 = new Class("semester 1", 30, c1);
        cl2 = new Class("semester 2", 20, c2);

        try {
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from Class").executeUpdate();
            em.createQuery("delete from Course").executeUpdate();
            em.persist(c1);
            em.persist(c2);
            em.persist(cl1);
            em.persist(cl2);
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
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from Class").executeUpdate();
            em.createQuery("delete from Course").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testAddClassFromFacade() throws API_Exception {
        //Arrange
        EntityManager em = emf.createEntityManager();
        ClassDTO[] classes = facade.getAllClasses();
        ClassDTO dto = classes[0];

        //Act
        ClassDTO newClass = facade.addClass(dto);

        //Assert
        assertNotNull(newClass.getId());
    }

    @Test
    public void addClassThrowsException() {
        //Arrange
        EntityManager em = emf.createEntityManager();
        ClassDTO[] classes = facade.getAllClasses();
        Class c = new Class(classes[0].getSemester(), classes[0].getNumberOfStudents(), new Course());
        ClassDTO dto = new ClassDTO(c);

        try{
            //Act
            facade.addClass(dto);
        } catch(API_Exception ex){
            //Assert
            assertEquals(400, ex.getErrorCode());
        }
    }

    @Test
    public void getAllClassesFacade() throws API_Exception {
        //Act
        ClassDTO[] classes = facade.getAllClasses();

        //Assert
        assertEquals(2, classes.length);
    }
}
