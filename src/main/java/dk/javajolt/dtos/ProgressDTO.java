package dk.javajolt.dtos;

import dk.javajolt.entities.Progress;

public class ProgressDTO {
    private Long id;
    private Long userId;
    private Long exerciseId;
    private boolean completed;
    private Integer score;
    private String createdAt;
    private String completedAt;

    public ProgressDTO() {}

    public ProgressDTO(Progress progress) {
        this.id = progress.getId();
        this.userId = progress.getUser() != null ? progress.getUser().getId() : null;
        this.exerciseId = progress.getExercise() != null ? progress.getExercise().getId() : null;
        this.completed = progress.isCompleted();
        this.score = progress.getScore();
        this.createdAt = progress.getCreatedAt() != null ? progress.getCreatedAt().toString() : null;
        this.completedAt = progress.getCompletedAt() != null ? progress.getCompletedAt().toString() : null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getExerciseId() { return exerciseId; }
    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getCompletedAt() { return completedAt; }
    public void setCompletedAt(String completedAt) { this.completedAt = completedAt; }
}