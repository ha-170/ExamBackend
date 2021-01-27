package rest;

import entities.Course;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import io.restassured.response.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class CourseResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Course c1, c2;
    private static User u1;
    private static Role r1;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.createQuery("delete from Course").executeUpdate();
            u1 = new User("admin","test1");
            r1 = new Role("admin");
            u1.addRole(r1);
            c1 = new Course("security", "beginners");
            c2 = new Course("javascript", "advanced");
            em.persist(r1);
            em.persist(u1);
            em.persist(c1);
            em.persist(c2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private static String token;

    private static void performLogin(String username, String password) {
        String body = String.format("{username: \"%s\", password: \"%s\"}", username, password);
        token = given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/login")
                .then()
                .extract()
                .path("token");
    }

    @Test
    public void testAddCourse(){
        performLogin("admin", "test1");
        String body = String.format("{courseName: \"%s\", description: \"%s\"}", "security", "beginners");
        Response response = given()
                            .contentType("application/json")
                            .header("x-access-token", token)
                            .body(body)
                            .when()
                            .post("/course/add")
                            .then()
                            .extract()
                            .response();
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("security", response.jsonPath().getString("courseName"));
    }

    @Test
    public void testAddCourseBadRequest(){
        performLogin("admin", "test1");
        String body = String.format("{courseName: \"%s\"}", "security");
        Response response = given()
                .contentType("application/json")
                .header("x-access-token", token)
                .body(body)
                .when()
                .post("/course/add")
                .then()
                .extract()
                .response();
        Assertions.assertEquals(400, response.statusCode());
    }

    @Test
    public void testGetAllCourses(){
        performLogin("admin", "test1");
        Response response = given()
                .contentType("application/json")
                .header("x-access-token", token)
                .when()
                .get("/course/all")
                .then()
                .extract()
                .response();
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(2, response.jsonPath().getList("$").size());

    }
}
