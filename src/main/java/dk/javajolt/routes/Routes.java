package dk.javajolt.routes;

import dk.javajolt.controllers.*;
import io.javalin.config.JavalinConfig;

public class Routes {

    private final SecurityController securityController = new SecurityController();
    private final UserController userController = new UserController();
    private final CourseController courseController = new CourseController();
    private final LessonController lessonController = new LessonController();
    private final ExerciseController exerciseController = new ExerciseController();
    private final ProgressController progressController = new ProgressController();
    private final StackOverflowController stackOverflowController = new StackOverflowController();

    public void addRoutes(JavalinConfig config) {
        config.routes.post("/api/auth/register", securityController::register);
        config.routes.post("/api/auth/login", securityController::login);

        config.routes.get("/api/users", userController::getAll);
        config.routes.post("/api/users", userController::create);
        config.routes.get("/api/users/{id}", userController::getById);
        config.routes.put("/api/users/{id}", userController::update);
        config.routes.delete("/api/users/{id}", userController::delete);
        config.routes.get("/api/users/{userId}/progress", progressController::getByUserId);

        config.routes.get("/api/courses", courseController::getAll);
        config.routes.post("/api/courses", courseController::create);
        config.routes.get("/api/courses/{id}", courseController::getById);
        config.routes.put("/api/courses/{id}", courseController::update);
        config.routes.delete("/api/courses/{id}", courseController::delete);
        config.routes.get("/api/courses/{courseId}/lessons", lessonController::getByCourseId);
        config.routes.post("/api/courses/{courseId}/lessons", lessonController::create);

        config.routes.get("/api/lessons/{id}", lessonController::getById);
        config.routes.put("/api/lessons/{id}", lessonController::update);
        config.routes.delete("/api/lessons/{id}", lessonController::delete);
        config.routes.get("/api/lessons/{lessonId}/exercises", exerciseController::getByLessonId);
        config.routes.post("/api/lessons/{lessonId}/exercises", exerciseController::create);

        config.routes.get("/api/exercises/{id}", exerciseController::getById);
        config.routes.put("/api/exercises/{id}", exerciseController::update);
        config.routes.delete("/api/exercises/{id}", exerciseController::delete);

        config.routes.get("/api/progress/{id}", progressController::getById);
        config.routes.post("/api/progress", progressController::create);
        config.routes.put("/api/progress/{id}/complete", progressController::markComplete);

        config.routes.get("/api/stackoverflow/questions", stackOverflowController::getQuestions);
    }
}