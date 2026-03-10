package dk.javajolt.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateConfig {

    private static EntityManagerFactory emf;

    private HibernateConfig() {}

    public static EntityManagerFactory getEntityManagerFactory() {

        if (emf == null) {

            String unit = System.getProperty("test") != null
                    ? "javajolt-test-pu"
                    : "javajolt-pu";

            emf = Persistence.createEntityManagerFactory(unit);
        }

        return emf;
    }

    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}