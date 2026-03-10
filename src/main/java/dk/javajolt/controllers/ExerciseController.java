package dk.javajolt.controllers;

import dk.javajolt.daos.ExerciseDAO;
import dk.javajolt.daos.LessonDAO;
import dk.javajolt.dtos.ExerciseDTO;
import dk.javajolt.entities.Exercise;
import dk.javajolt.entities.Lesson;
import dk.javajolt.exceptions.NotFoundException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ExerciseController {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseController.class);
    private final ExerciseDAO exerciseDAO;
    private final LessonDAO lessonDAO;

    public ExerciseController() {
        this.exerciseDAO = ExerciseDAO.getInstance();
        this.lessonDAO = LessonDAO.getInstance();
    }

    public void getByLessonId(Context ctx) {
        Long lessonId = Long.parseLong(ctx.pathParam("lessonId"));
        logger.info("GET /api/lessons/{}/exercises", lessonId);
        List<ExerciseDTO> dtos = exerciseDAO.findByLessonId(lessonId).stream()
                .map(ExerciseDTO::new).collect(Collectors.toList());
        ctx.json(dtos).status(200);
    }

    public void getById(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        logger.info("GET /api/exercises/{}", id);
        Exercise exercise = exerciseDAO.findById(id);
        if (exercise == null) throw new NotFoundException("Exercise not found with id: " + id);
        ctx.json(new ExerciseDTO(exercise)).status(200);
    }

    public void create(Context ctx) {
        Long lessonId = Long.parseLong(ctx.pathParam("lessonId"));
        logger.info("POST /api/lessons/{}/exercises", lessonId);
        Lesson lesson = lessonDAO.findById(lessonId);
        if (lesson == null) throw new NotFoundException("Lesson not found with id: " + lessonId);
        ExerciseDTO body = ctx.bodyAsClass(ExerciseDTO.class);
        Exercise created = exerciseDAO.create(new Exercise(body.getTitle(), body.getDescription(), body.getStarterCode(), body.getSolution(), body.getDifficulty(), lesson));
        ctx.json(new ExerciseDTO(created)).status(201);
    }

    public void update(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        logger.info("PUT /api/exercises/{}", id);
        Exercise exercise = exerciseDAO.findById(id);
        if (exercise == null) throw new NotFoundException("Exercise not found with id: " + id);
        ExerciseDTO body = ctx.bodyAsClass(ExerciseDTO.class);
        if (body.getTitle() != null) exercise.setTitle(body.getTitle());
        if (body.getDescription() != null) exercise.setDescription(body.getDescription());
        if (body.getStarterCode() != null) exercise.setStarterCode(body.getStarterCode());
        if (body.getSolution() != null) exercise.setSolution(body.getSolution());
        if (body.getDifficulty() != null) exercise.setDifficulty(body.getDifficulty());
        ctx.json(new ExerciseDTO(exerciseDAO.update(exercise))).status(200);
    }

    public void delete(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        logger.info("DELETE /api/exercises/{}", id);
        if (exerciseDAO.findById(id) == null) throw new NotFoundException("Exercise not found with id: " + id);
        exerciseDAO.delete(id);
        ctx.status(204);
    }
}