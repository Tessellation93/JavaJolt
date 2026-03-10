package dk.javajolt;

import dk.javajolt.config.HibernateConfig;
import dk.javajolt.daos.CourseDAO;
import dk.javajolt.daos.ExerciseDAO;
import dk.javajolt.daos.LessonDAO;
import dk.javajolt.entities.Course;
import dk.javajolt.entities.Exercise;
import dk.javajolt.entities.Lesson;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExerciseDAOTest {

    private static ExerciseDAO exerciseDAO;
    private static CourseDAO courseDAO;
    private static LessonDAO lessonDAO;
    private Lesson testLesson;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("test", "true");
        HibernateConfig.getEntityManagerFactory();
        exerciseDAO = ExerciseDAO.getInstance();
        courseDAO = CourseDAO.getInstance();
        lessonDAO = LessonDAO.getInstance();
    }

    @BeforeEach
    public void setUp() {
        try (var em = HibernateConfig.getEntityManagerFactory().createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE progress, exercises, lessons, courses, users CASCADE").executeUpdate();
            em.getTransaction().commit();
        }
        Course course = courseDAO.create(new Course("Exercise Test Course", "Java", "Test", "BEGINNER"));
        testLesson = lessonDAO.create(new Lesson("Exercise Test Lesson", "Content", 1, course));
    }

    @Test
    public void testCreateExercise() {
        Exercise exercise = new Exercise("Exercise 1", "Description 1", "starter code", "solution", "BEGINNER", testLesson);
        Exercise created = exerciseDAO.create(exercise);
        assertNotNull(created.getId());
        assertEquals("Exercise 1", created.getTitle());
    }

    @Test
    public void testFindById() {
        Exercise exercise = new Exercise("Exercise 2", "Description 2", null, "solution", "INTERMEDIATE", testLesson);
        Exercise created = exerciseDAO.create(exercise);
        Exercise found = exerciseDAO.findById(created.getId());
        assertNotNull(found);
        assertEquals("Exercise 2", found.getTitle());
    }

    @Test
    public void testFindByLessonId() {
        exerciseDAO.create(new Exercise("Exercise 3", "Description 3", null, "solution", "BEGINNER", testLesson));
        List<Exercise> exercises = exerciseDAO.findByLessonId(testLesson.getId());
        assertTrue(exercises.size() > 0);
    }

    @Test
    public void testUpdateExercise() {
        Exercise exercise = new Exercise("Update Test", "Description", null, "solution", "BEGINNER", testLesson);
        Exercise created = exerciseDAO.create(exercise);
        created.setTitle("Updated Exercise");
        Exercise updated = exerciseDAO.update(created);
        assertEquals("Updated Exercise", updated.getTitle());
    }

    @Test
    public void testSoftDelete() {
        Exercise exercise = new Exercise("Delete Test", "Description", null, "solution", "BEGINNER", testLesson);
        Exercise created = exerciseDAO.create(exercise);
        Long id = created.getId();
        exerciseDAO.delete(id);
        Exercise found = exerciseDAO.findById(id);
        assertNull(found);
    }
}