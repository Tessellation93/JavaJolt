package dk.javajolt.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = hashPassword(password);
        this.isAdmin = false;
        this.isDeleted = false;
    }

    public User(String username, String email, String password, boolean isAdmin) {
        this(username, email, password);
        this.isAdmin = isAdmin;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void setPassword(String plainPassword) {
        this.password = hashPassword(plainPassword);
    }

    public boolean verifyPassword(String plainPassword) {
        return BCrypt.checkpw(plainPassword, this.password);
    }

    public void addRole(Role role) {
        this.roles.add(role);
        if (role.getName().equals("ADMIN")) {
            this.isAdmin = true;
        }
    }

    public void removeRole(String roleName) {
        this.roles.removeIf(r -> r.getName().equals(roleName.toUpperCase()));
        if (roleName.equalsIgnoreCase("ADMIN")) {
            this.isAdmin = false;
        }
    }

    public Set<String> getRolesAsStrings() {
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email +
                "', isAdmin=" + isAdmin + ", isDeleted=" + isDeleted + "}";
    }
}