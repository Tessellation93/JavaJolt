package dk.javajolt;

import dk.javajolt.config.HibernateConfig;
import dk.javajolt.daos.CourseDAO;
import dk.javajolt.daos.ExerciseDAO;
import dk.javajolt.daos.LessonDAO;
import dk.javajolt.daos.ProgressDAO;
import dk.javajolt.daos.UserDAO;
import dk.javajolt.entities.Course;
import dk.javajolt.entities.Exercise;
import dk.javajolt.entities.Lesson;
import dk.javajolt.entities.Progress;
import dk.javajolt.entities.User;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProgressDAOTest {

    private static ProgressDAO progressDAO;
    private static UserDAO userDAO;
    private static CourseDAO courseDAO;
    private static LessonDAO lessonDAO;
    private static ExerciseDAO exerciseDAO;

    private User testUser;
    private Exercise testExercise;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("test", "true");
        HibernateConfig.getEntityManagerFactory();
        progressDAO = ProgressDAO.getInstance();
        userDAO = UserDAO.getInstance();
        courseDAO = CourseDAO.getInstance();
        lessonDAO = LessonDAO.getInstance();
        exerciseDAO = ExerciseDAO.getInstance();
    }

    @BeforeEach
    public void setUp() {
        try (var em = HibernateConfig.getEntityManagerFactory().createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE progress, exercises, lessons, courses, users CASCADE").executeUpdate();
            em.getTransaction().commit();
        }
        testUser = userDAO.create(new User("progressuser", "progress@example.com", "password123"));
        Course course = courseDAO.create(new Course("Progress Test Course", "Java", "Test", "BEGINNER"));
        Lesson lesson = lessonDAO.create(new Lesson("Progress Test Lesson", "Content", 1, course));
        testExercise = exerciseDAO.create(new Exercise("Progress Test Exercise", "Description", null, "solution", "BEGINNER", lesson));
    }

    @Test
    public void testCreateProgress() {
        Progress progress = new Progress(testUser, testExercise);
        Progress created = progressDAO.create(progress);
        assertNotNull(created.getId());
        assertFalse(created.isCompleted());
    }

    @Test
    public void testFindById() {
        Progress progress = new Progress(testUser, testExercise);
        Progress created = progressDAO.create(progress);
        Progress found = progressDAO.findById(created.getId());
        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
    }

    @Test
    public void testFindByUserId() {
        progressDAO.create(new Progress(testUser, testExercise));
        List<Progress> progressList = progressDAO.findByUserId(testUser.getId());
        assertTrue(progressList.size() > 0);
    }

    @Test
    public void testMarkComplete() {
        Progress progress = new Progress(testUser, testExercise);
        Progress created = progressDAO.create(progress);
        created.setCompleted(true);
        created.setScore(95);
        created.setCompletedAt(LocalDateTime.now());
        Progress updated = progressDAO.update(created);
        assertTrue(updated.isCompleted());
        assertEquals(95, updated.getScore());
        assertNotNull(updated.getCompletedAt());
    }

    @Test
    public void testFindByUserAndExercise() {
        progressDAO.create(new Progress(testUser, testExercise));
        Progress found = progressDAO.findByUserAndExercise(testUser.getId(), testExercise.getId());
        assertNotNull(found);
    }
}