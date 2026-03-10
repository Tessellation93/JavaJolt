package dk.javajolt.daos;

import dk.javajolt.entities.Lesson;
import jakarta.persistence.EntityManager;

import java.util.List;

public class LessonDAO extends BaseDAO<Lesson> {

    private static LessonDAO instance;

    private LessonDAO() {
        super(Lesson.class);
    }

    public static LessonDAO getInstance() {
        if (instance == null) {
            instance = new LessonDAO();
        }
        return instance;
    }

    public List<Lesson> findByCourseId(Long courseId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT l FROM Lesson l WHERE l.course.id = :courseId AND l.isDeleted = false ORDER BY l.orderIndex", Lesson.class)
                    .setParameter("courseId", courseId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
