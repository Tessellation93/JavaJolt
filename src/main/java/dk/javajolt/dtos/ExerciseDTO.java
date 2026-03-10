package dk.javajolt.dtos;

import dk.javajolt.entities.Exercise;

public class ExerciseDTO {
    private Long id;
    private String title;
    private String description;
    private String starterCode;
    private String solution;
    private String difficulty;
    private Long lessonId;
    private String createdAt;

    public ExerciseDTO() {}

    public ExerciseDTO(Exercise exercise) {
        this.id = exercise.getId();
        this.title = exercise.getTitle();
        this.description = exercise.getDescription();
        this.starterCode = exercise.getStarterCode();
        this.difficulty = exercise.getDifficulty();
        this.lessonId = exercise.getLesson() != null ? exercise.getLesson().getId() : null;
        this.createdAt = exercise.getCreatedAt() != null ? exercise.getCreatedAt().toString() : null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStarterCode() { return starterCode; }
    public void setStarterCode(String starterCode) { this.starterCode = starterCode; }
    public String getSolution() { return solution; }
    public void setSolution(String solution) { this.solution = solution; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}