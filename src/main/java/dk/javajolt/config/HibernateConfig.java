package dk.javajolt.config;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
public class HibernateConfig {
    private static volatile EntityManagerFactory emf;
    private HibernateConfig() {}
    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            if (System.getProperty("test") != null) {
                emf = Persistence.createEntityManagerFactory("javajolt-test-pu");
            } else if (System.getenv("DEPLOYED") != null) {
                Map<String, String> props = new HashMap<>();
                props.put("jakarta.persistence.jdbc.url",
                        String.format(System.getenv("JDBC_CONNECTION_STRING"), System.getenv("JDBC_DB")));
                props.put("jakarta.persistence.jdbc.user", System.getenv("JDBC_USER"));
                props.put("jakarta.persistence.jdbc.password", System.getenv("JDBC_PASSWORD"));
                emf = Persistence.createEntityManagerFactory("javajolt-pu", props);
            } else {
                emf = Persistence.createEntityManagerFactory("javajolt-pu");
            }
        }
        return emf;
    }
    //    private static EntityManagerFactory emf;
//    public static EntityManagerFactory getEntityManagerFactory() {
//        if (emf == null) {
//            ...
//        }
//        return emf;
//    }
    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}