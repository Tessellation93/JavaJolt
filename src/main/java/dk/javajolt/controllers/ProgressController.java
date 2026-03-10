package dk.javajolt.controllers;

import dk.javajolt.dtos.ProgressDTO;
import dk.javajolt.services.ProgressService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ProgressController {

    private static final Logger logger = LoggerFactory.getLogger(ProgressController.class);
    private final ProgressService progressService;

    public ProgressController() {
        this.progressService = new ProgressService();
    }

    public void getByUserId(Context ctx) {
        Long userId = Long.parseLong(ctx.pathParam("userId"));
        logger.info("GET /api/users/{}/progress", userId);
        List<ProgressDTO> progress = progressService.getProgressByUserId(userId);
        ctx.json(progress).status(200);
    }

    public void getById(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        logger.info("GET /api/progress/{}", id);
        ctx.json(progressService.getProgressById(id)).status(200);
    }

    public void create(Context ctx) {
        logger.info("POST /api/progress");
        ProgressDTO body = ctx.bodyAsClass(ProgressDTO.class);
        ctx.json(progressService.createProgress(body.getUserId(), body.getExerciseId())).status(201);
    }

    public void markComplete(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        logger.info("PUT /api/progress/{}/complete", id);
        ProgressDTO body = ctx.bodyAsClass(ProgressDTO.class);
        ctx.json(progressService.markComplete(id, body.getScore())).status(200);
    }
}