package dk.javajolt;

import dk.javajolt.config.HibernateConfig;
import dk.javajolt.daos.CourseDAO;
import dk.javajolt.entities.Course;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseDAOTest {

    private static CourseDAO courseDAO;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("test", "true");
        HibernateConfig.getEntityManagerFactory();
        courseDAO = CourseDAO.getInstance();
    }

    @BeforeEach
    public void setUp() {
        try (var em = HibernateConfig.getEntityManagerFactory().createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE progress, exercises, lessons, courses, users CASCADE").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    public void testCreateCourse() {
        Course course = new Course("Java Basics", "Java", "Learn Java fundamentals", "BEGINNER");
        Course created = courseDAO.create(course);
        assertNotNull(created.getId());
        assertEquals("Java Basics", created.getName());
    }

    @Test
    public void testFindById() {
        Course course = new Course("Python Basics", "Python", "Learn Python", "BEGINNER");
        Course created = courseDAO.create(course);
        Course found = courseDAO.findById(created.getId());
        assertNotNull(found);
        assertEquals("Python Basics", found.getName());
    }

    @Test
    public void testFindByLanguage() {
        courseDAO.create(new Course("Java Advanced", "Java", "Advanced Java", "ADVANCED"));
        List<Course> javaCourses = courseDAO.findByLanguage("Java");
        assertTrue(javaCourses.size() > 0);
    }

    @Test
    public void testFindByDifficulty() {
        courseDAO.create(new Course("Beginner Course", "JavaScript", "JS Basics", "BEGINNER"));
        List<Course> beginnerCourses = courseDAO.findByDifficulty("BEGINNER");
        assertTrue(beginnerCourses.size() > 0);
    }

    @Test
    public void testUpdateCourse() {
        Course course = new Course("Update Test", "Java", "Test", "BEGINNER");
        Course created = courseDAO.create(course);
        created.setName("Updated Name");
        Course updated = courseDAO.update(created);
        assertEquals("Updated Name", updated.getName());
    }
}