package dk.javajolt;

import dk.javajolt.config.HibernateConfig;
import dk.javajolt.daos.CourseDAO;
import dk.javajolt.daos.LessonDAO;
import dk.javajolt.entities.Course;
import dk.javajolt.entities.Lesson;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LessonDAOTest {

    private static LessonDAO lessonDAO;
    private static CourseDAO courseDAO;
    private Course testCourse;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("test", "true");
        HibernateConfig.getEntityManagerFactory();
        lessonDAO = LessonDAO.getInstance();
        courseDAO = CourseDAO.getInstance();
    }

    @BeforeEach
    public void setUp() {
        try (var em = HibernateConfig.getEntityManagerFactory().createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE progress, exercises, lessons, courses, users CASCADE").executeUpdate();
            em.getTransaction().commit();
        }
        testCourse = courseDAO.create(new Course("Test Course", "Java", "Test", "BEGINNER"));
    }

    @Test
    public void testCreateLesson() {
        Lesson lesson = new Lesson("Lesson 1", "Content 1", 1, testCourse);
        Lesson created = lessonDAO.create(lesson);
        assertNotNull(created.getId());
        assertEquals("Lesson 1", created.getTitle());
    }

    @Test
    public void testFindById() {
        Lesson lesson = new Lesson("Lesson 2", "Content 2", 2, testCourse);
        Lesson created = lessonDAO.create(lesson);
        Lesson found = lessonDAO.findById(created.getId());
        assertNotNull(found);
        assertEquals("Lesson 2", found.getTitle());
    }

    @Test
    public void testFindByCourseId() {
        lessonDAO.create(new Lesson("Lesson 3", "Content 3", 3, testCourse));
        List<Lesson> lessons = lessonDAO.findByCourseId(testCourse.getId());
        assertTrue(lessons.size() > 0);
    }

    @Test
    public void testOrderIndex() {
        Lesson lesson = new Lesson("Ordered Lesson", "Content", 5, testCourse);
        Lesson created = lessonDAO.create(lesson);
        assertEquals(5, created.getOrderIndex());
    }

    @Test
    public void testUpdateLesson() {
        Lesson lesson = new Lesson("Update Test", "Content", 1, testCourse);
        Lesson created = lessonDAO.create(lesson);
        created.setTitle("Updated Title");
        Lesson updated = lessonDAO.update(created);
        assertEquals("Updated Title", updated.getTitle());
    }

    @Test
    public void testSoftDelete() {
        Lesson lesson = new Lesson("Delete Test", "Content", 1, testCourse);
        Lesson created = lessonDAO.create(lesson);
        Long id = created.getId();
        lessonDAO.delete(id);
        Lesson found = lessonDAO.findById(id);
        assertNull(found);
    }
}