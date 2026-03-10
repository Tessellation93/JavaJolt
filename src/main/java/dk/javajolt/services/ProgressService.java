package dk.javajolt.services;

import dk.javajolt.daos.ExerciseDAO;
import dk.javajolt.daos.ProgressDAO;
import dk.javajolt.daos.UserDAO;
import dk.javajolt.dtos.ProgressDTO;
import dk.javajolt.entities.Exercise;
import dk.javajolt.entities.Progress;
import dk.javajolt.entities.User;
import dk.javajolt.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProgressService {

    private final ProgressDAO progressDAO;
    private final UserDAO userDAO;
    private final ExerciseDAO exerciseDAO;

    public ProgressService() {
        this.progressDAO = ProgressDAO.getInstance();
        this.userDAO = UserDAO.getInstance();
        this.exerciseDAO = ExerciseDAO.getInstance();
    }

    public ProgressDTO createProgress(Long userId, Long exerciseId) {
        User user = userDAO.findById(userId);
        if (user == null) throw new NotFoundException("User not found with id: " + userId);
        Exercise exercise = exerciseDAO.findById(exerciseId);
        if (exercise == null) throw new NotFoundException("Exercise not found with id: " + exerciseId);
        Progress created = progressDAO.create(new Progress(user, exercise));
        return new ProgressDTO(created);
    }

    public ProgressDTO getProgressById(Long id) {
        Progress progress = progressDAO.findById(id);
        if (progress == null) throw new NotFoundException("Progress not found with id: " + id);
        return new ProgressDTO(progress);
    }

    public List<ProgressDTO> getProgressByUserId(Long userId) {
        return progressDAO.findByUserId(userId).stream()
                .map(ProgressDTO::new)
                .collect(Collectors.toList());
    }

    public ProgressDTO markComplete(Long id, Integer score) {
        Progress progress = progressDAO.findById(id);
        if (progress == null) throw new NotFoundException("Progress not found with id: " + id);
        progress.setCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());
        if (score != null) progress.setScore(score);
        return new ProgressDTO(progressDAO.update(progress));
    }

    public ProgressDTO updateScore(Long id, Integer score) {
        Progress progress = progressDAO.findById(id);
        if (progress == null) throw new NotFoundException("Progress not found with id: " + id);
        progress.setScore(score);
        return new ProgressDTO(progressDAO.update(progress));
    }
}