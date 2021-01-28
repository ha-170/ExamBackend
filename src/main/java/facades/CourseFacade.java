package facades;

import dto.CourseDTO;
import entities.Course;
import errorhandling.API_Exception;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import javax.persistence.TypedQuery;

public class CourseFacade {

    private static EntityManagerFactory emf;
    private static CourseFacade instance;

    private CourseFacade() {
    }
    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static CourseFacade getCourseFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CourseFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public CourseDTO addCourse(CourseDTO courseDTO) throws API_Exception {
        EntityManager em = emf.createEntityManager();
        if (courseDTO.getCourseName()==null || courseDTO.getDescription()==null){
            throw new API_Exception("Course name or description is missing.");
        }
        Course course = new Course(courseDTO.getCourseName(), courseDTO.getDescription());
        try {
            em.getTransaction().begin();
            em.persist(course);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new CourseDTO(course);
    }

    public CourseDTO[] getAllCourses() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Course> query = em.createQuery("SELECT c FROM Course c", Course.class);
            List<Course> courses = query.getResultList();
            CourseDTO[] courseArray = new CourseDTO[courses.size()];
            for(int i = 0; i < courseArray.length; i++){
                Course c = courses.get(i);
                courseArray[i] = new CourseDTO(c);
            }
        return courseArray;
        } finally {
            em.close();
        }
    }
}


