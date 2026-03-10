package dk.javajolt.dtos;

import dk.javajolt.entities.Course;

public class CourseDTO {
    private Long id;
    private String name;
    private String programmingLanguage;
    private String description;
    private String difficulty;
    private String createdAt;

    public CourseDTO() {}

    public CourseDTO(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.programmingLanguage = course.getProgrammingLanguage();
        this.description = course.getDescription();
        this.difficulty = course.getDifficulty() != null ? course.getDifficulty().name() : null;
        this.createdAt = course.getCreatedAt() != null ? course.getCreatedAt().toString() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
