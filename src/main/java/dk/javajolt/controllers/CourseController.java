package dk.javajolt.controllers;

import dk.javajolt.dtos.CourseDTO;
import dk.javajolt.exceptions.NotFoundException;
import dk.javajolt.services.CourseService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;

    public CourseController() {
        this.courseService = new CourseService();
    }

    public void getAll(Context ctx) {
        logger.info("GET /api/courses");
        String language = ctx.queryParam("language");
        String difficulty = ctx.queryParam("difficulty");

        List<CourseDTO> courses;
        if (language != null) {
            courses = courseService.getCoursesByLanguage(language);
        } else if (difficulty != null) {
            courses = courseService.getCoursesByDifficulty(difficulty);
        } else {
            courses = courseService.getAllCourses();
        }
        ctx.json(courses).status(200);
    }

    public void getById(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        logger.info("GET /api/courses/{}", id);
        ctx.json(courseService.getCourseById(id)).status(200);
    }

    public void create(Context ctx) {
        logger.info("POST /api/courses");
        CourseDTO body = ctx.bodyAsClass(CourseDTO.class);
        ctx.json(courseService.createCourse(body.getName(), body.getProgrammingLanguage(), body.getDescription(), body.getDifficulty())).status(201);
    }

    public void update(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        logger.info("PUT /api/courses/{}", id);
        CourseDTO body = ctx.bodyAsClass(CourseDTO.class);
        ctx.json(courseService.updateCourse(id, body.getName(), body.getProgrammingLanguage(), body.getDescription(), body.getDifficulty())).status(200);
    }

    public void delete(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        logger.info("DELETE /api/courses/{}", id);
        courseService.deleteCourse(id);
        ctx.status(204);
    }
}