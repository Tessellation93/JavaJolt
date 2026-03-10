package dk.javajolt.daos;

import dk.javajolt.entities.Exercise;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ExerciseDAO extends BaseDAO<Exercise> {

    private static ExerciseDAO instance;

    private ExerciseDAO() {
        super(Exercise.class);
    }

    public static ExerciseDAO getInstance() {
        if (instance == null) {
            instance = new ExerciseDAO();
        }
        return instance;
    }

    public List<Exercise> findByLessonId(Long lessonId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT e FROM Exercise e WHERE e.lesson.id = :lessonId AND e.isDeleted = false", Exercise.class)
                    .setParameter("lessonId", lessonId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
