package dk.javajolt.daos;

import dk.javajolt.config.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public abstract class BaseDAO<T> implements IDAO<T> {

    protected static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    protected final Class<T> entityClass;

    protected BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T create(T entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to create: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public T findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            T entity = em.find(entityClass, id);
            if (entity != null && !isDeleted(entity)) {
                return entity;
            }
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            String entityName = entityClass.getSimpleName();
            TypedQuery<T> query = em.createQuery(
                    "SELECT e FROM " + entityName + " e WHERE e.isDeleted = false ORDER BY e.createdAt DESC",
                    entityClass
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public T update(T entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            T updated = em.merge(entity);
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                setDeleted(entity, true);
                em.merge(entity);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = emf.createEntityManager();
        try {
            String entityName = entityClass.getSimpleName();
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(e) FROM " + entityName + " e WHERE e.isDeleted = false",
                    Long.class
            );
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    protected boolean isDeleted(T entity) {
        try {
            return (boolean) entity.getClass().getMethod("isDeleted").invoke(entity);
        } catch (Exception e) {
            return false;
        }
    }

    protected void setDeleted(T entity, boolean deleted) {
        try {
            entity.getClass().getMethod("setDeleted", boolean.class).invoke(entity, deleted);
        } catch (Exception e) {
            throw new RuntimeException("Entity must have setDeleted method", e);
        }
    }
}