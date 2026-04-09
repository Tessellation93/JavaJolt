package dk.javajolt.controllers;

import dk.javajolt.dtos.StackOverflowQuestionDTO;
import dk.javajolt.services.StackOverflowService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class StackOverflowController {

    private static final Logger logger = LoggerFactory.getLogger(StackOverflowController.class);
    private final StackOverflowService stackOverflowService;

    public StackOverflowController() {
        this.stackOverflowService = new StackOverflowService();
    }

    public void getQuestions(Context ctx) {
        int pageSize = ctx.queryParamAsClass("size", Integer.class).getOrDefault(10);
        if (pageSize > 25) pageSize = 25;

        logger.info("GET /api/stackoverflow/questions?size={}", pageSize);

        try {
            List<StackOverflowQuestionDTO> questions = stackOverflowService.fetchQuestions("java", pageSize);
            ctx.json(questions).status(200);
        } catch (Exception e) {
            logger.error("Failed to fetch Stack Overflow questions: {}", e.getMessage());
            ctx.status(502).json(Map.of("error", "Failed to fetch questions from Stack Overflow"));
        }
    }
}
