package dk.javajolt;

import dk.javajolt.config.HibernateConfig;
import dk.javajolt.daos.UserDAO;
import dk.javajolt.entities.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    private static UserDAO userDAO;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("test", "true");
        HibernateConfig.getEntityManagerFactory();
        userDAO = UserDAO.getInstance();
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
    public void testCreateUser() {
        User user = new User("testuser", "test@example.com", "password123", false);
        User created = userDAO.create(user);
        assertNotNull(created.getId());
        assertEquals("testuser", created.getUsername());
    }

    @Test
    public void testFindById() {
        User user = new User("findtest", "find@example.com", "password123", false);
        User created = userDAO.create(user);
        User found = userDAO.findById(created.getId());
        assertNotNull(found);
        assertEquals("findtest", found.getUsername());
    }

    @Test
    public void testFindByEmail() {
        User user = new User("emailtest", "email@example.com", "password123", false);
        userDAO.create(user);
        User found = userDAO.findByEmail("email@example.com");
        assertNotNull(found);
        assertEquals("emailtest", found.getUsername());
    }

    @Test
    public void testUpdateUser() {
        User user = new User("updatetest", "update@example.com", "password123", false);
        User created = userDAO.create(user);
        created.setUsername("updateduser");
        User updated = userDAO.update(created);
        assertEquals("updateduser", updated.getUsername());
    }

    @Test
    public void testPasswordHashing() {
        User user = new User("hashtest", "hash@example.com", "mypassword", false);
        User created = userDAO.create(user);
        assertNotEquals("mypassword", created.getPassword());
        assertTrue(created.verifyPassword("mypassword"));
    }

    @Test
    public void testSoftDelete() {
        User user = new User("deletetest", "delete@example.com", "password123", false);
        User created = userDAO.create(user);
        Long id = created.getId();
        userDAO.delete(id);
        User found = userDAO.findById(id);
        assertNull(found);
    }
}