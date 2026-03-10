package dk.javajolt.dtos;

import dk.javajolt.entities.Lesson;

public class LessonDTO {
    private Long id;
    private String title;
    private String content;
    private int orderIndex;
    private Long courseId;
    private String createdAt;

    public LessonDTO() {}

    public LessonDTO(Lesson lesson) {
        this.id = lesson.getId();
        this.title = lesson.getTitle();
        this.content = lesson.getContent();
        this.orderIndex = lesson.getOrderIndex();
        this.courseId = lesson.getCourse() != null ? lesson.getCourse().getId() : null;
        this.createdAt = lesson.getCreatedAt() != null ? lesson.getCreatedAt().toString() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
