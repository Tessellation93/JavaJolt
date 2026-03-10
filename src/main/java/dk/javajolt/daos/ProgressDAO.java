package dk.javajolt.daos;

import dk.javajolt.entities.Progress;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ProgressDAO extends BaseDAO<Progress> {

    private static ProgressDAO instance;

    private ProgressDAO() {
        super(Progress.class);
    }

    public static ProgressDAO getInstance() {
        if (instance == null) {
            instance = new ProgressDAO();
        }
        return instance;
    }

    public Progress findByUserAndExercise(Long userId, Long exerciseId) {
        try (var em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT p FROM Progress p WHERE p.user.id = :userId AND p.exercise.id = :exerciseId AND p.isDeleted = false",
                            Progress.class)
                    .setParameter("userId", userId)
                    .setParameter("exerciseId", exerciseId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        }
    }

    public List<Progress> findByUserId(Long userId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Progress p WHERE p.user.id = :userId", Progress.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}