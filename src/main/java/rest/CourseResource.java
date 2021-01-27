package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.CourseDTO;
import errorhandling.API_Exception;
import facades.CourseFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("course")
public class CourseResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final CourseFacade FACADE = CourseFacade.getCourseFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @POST
    @Path("/add")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addCourse(String courseJson) throws API_Exception {
        CourseDTO course = GSON.fromJson(courseJson, CourseDTO.class);
        CourseDTO newCourse;
        try {
            newCourse = FACADE.addCourse(course);
        } catch (API_Exception ex) {
            throw ex;
        }
        return GSON.toJson(newCourse);
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCourses() {
        CourseDTO[] courses = FACADE.getAllCourses();
        return GSON.toJson(courses);
    }

}
