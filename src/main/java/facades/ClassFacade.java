package facades;

import dto.ClassDTO;
import entities.Course;
import entities.Class;
import errorhandling.API_Exception;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class ClassFacade {
    private static EntityManagerFactory emf;
    private static ClassFacade instance;

    private ClassFacade() {

    }
    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static ClassFacade getClassFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ClassFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    public ClassDTO addClass(ClassDTO classDTO) throws API_Exception {
        EntityManager em = emf.createEntityManager();
        if (classDTO.getSemester()==null || classDTO.getCourse()==null || classDTO.getCourse().getId()==null){
            throw new API_Exception("Semester or course is missing.");
        }
        Class c = new Class(classDTO.getSemester(), classDTO.getNumberOfStudents(), new Course (classDTO.getCourse().getId()));
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new ClassDTO(c);
    }

    public ClassDTO[] getAllClasses() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Class> query = em.createQuery("SELECT c FROM Class c", Class.class);
            List<Class> classes = query.getResultList();
            ClassDTO[] classArray = new ClassDTO[classes.size()];
            for(int i = 0; i < classArray.length; i++){
                Class c = classes.get(i);
                classArray[i] = new ClassDTO(c);
            }
            return classArray;
        } finally {
            em.close();
        }
    }
}
