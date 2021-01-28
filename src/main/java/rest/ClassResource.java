package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ClassDTO;
import errorhandling.API_Exception;
import facades.ClassFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("class")
public class ClassResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final ClassFacade FACADE = ClassFacade.getClassFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @POST
    @Path("/add")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addClass(String classJson) throws API_Exception {
        ClassDTO classDTO = GSON.fromJson(classJson, ClassDTO.class);
        ClassDTO newClass;
        try {
            newClass = FACADE.addClass(classDTO);
        } catch (API_Exception ex) {
            throw ex;
        }
        return GSON.toJson(newClass);
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllClasses() {
        ClassDTO[] classes = FACADE.getAllClasses();
        return GSON.toJson(classes);
    }

}

