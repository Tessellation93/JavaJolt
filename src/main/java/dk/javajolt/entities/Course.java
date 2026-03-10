package dk.javajolt.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "programming_language", nullable = false, length = 50)
    private String programmingLanguage;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Difficulty difficulty;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lesson> lessons = new ArrayList<>();

    public Course(String name, String programmingLanguage, String description, Difficulty difficulty) {
        this.name = name;
        this.programmingLanguage = programmingLanguage;
        this.description = description;
        this.difficulty = difficulty;
    }

    public Course(String name, String programmingLanguage, String description, String difficulty) {
        this(name, programmingLanguage, description, Difficulty.valueOf(difficulty.toUpperCase()));
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public enum Difficulty {
        BEGINNER, INTERMEDIATE, ADVANCED
    }
}