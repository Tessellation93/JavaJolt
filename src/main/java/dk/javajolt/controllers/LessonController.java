package dk.javajolt.controllers;

import dk.javajolt.daos.CourseDAO;
import dk.javajolt.daos.LessonDAO;
import dk.javajolt.dtos.LessonDTO;
import dk.javajolt.entities.Course;
import dk.javajolt.entities.Lesson;
import dk.javajolt.exceptions.NotFoundException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class LessonController {

    private static final Logger logger = LoggerFactory.getLogger(LessonController.class);
    private final LessonDAO lessonDAO;
    private final CourseDAO courseDAO;

    public LessonController() {
        this.lessonDAO = LessonDAO.getInstance();
        this.courseDAO = CourseDAO.getInstance();
    }

    public void getByCourseId(Context ctx) {
        Long courseId = Long.parseLong(ctx.pathParam("courseId"));
        logger.info("GET /api/courses/{}/lessons", courseId);
        List<LessonDTO> dtos = lessonDAO.findByCourseId(courseId).stream()
                .map(LessonDTO::new).collect(Collectors.toList());
        ctx.json(dtos).status(200);
    }

    public void getById(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        logger.info("GET /api/lessons/{}", id);
        Lesson lesson = lessonDAO.findById(id);
        if (lesson == null) throw new NotFoundException("Lesson not found with id: " + id);
        ctx.json(new LessonDTO(lesson)).status(200);
    }

    public void create(Context ctx) {
        Long courseId = Long.parseLong(ctx.pathParam("courseId"));
        logger.info("POST /api/courses/{}/lessons", courseId);
        Course course = courseDAO.findById(courseId);
        if (course == null) throw new NotFoundException("Course not found with id: " + courseId);
        LessonDTO body = ctx.bodyAsClass(LessonDTO.class);
        Lesson created = lessonDAO.create(new Lesson(body.getTitle(), body.getContent(), body.getOrderIndex(), course));
        ctx.json(new LessonDTO(created)).status(201);
    }

    public void update(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        logger.info("PUT /api/lessons/{}", id);
        Lesson lesson = lessonDAO.findById(id);
        if (lesson == null) throw new NotFoundException("Lesson not found with id: " + id);
        LessonDTO body = ctx.bodyAsClass(LessonDTO.class);
        if (body.getTitle() != null) lesson.setTitle(body.getTitle());
        if (body.getContent() != null) lesson.setContent(body.getContent());
        if (body.getOrderIndex() > 0) lesson.setOrderIndex(body.getOrderIndex());
        ctx.json(new LessonDTO(lessonDAO.update(lesson))).status(200);
    }

    public void delete(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        logger.info("DELETE /api/lessons/{}", id);
        if (lessonDAO.findById(id) == null) throw new NotFoundException("Lesson not found with id: " + id);
        lessonDAO.delete(id);
        ctx.status(204);
    }
}