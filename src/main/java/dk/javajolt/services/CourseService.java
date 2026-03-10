package dk.javajolt.services;

import dk.javajolt.daos.CourseDAO;
import dk.javajolt.dtos.CourseDTO;
import dk.javajolt.entities.Course;
import dk.javajolt.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class CourseService {

    private final CourseDAO courseDAO;

    public CourseService() {
        this.courseDAO = CourseDAO.getInstance();
    }

    public CourseDTO createCourse(String name, String programmingLanguage, String description, String difficulty) {
        Course.Difficulty diff = Course.Difficulty.valueOf(difficulty.toUpperCase());
        Course course = new Course(name, programmingLanguage, description, diff);
        Course created = courseDAO.create(course);
        return new CourseDTO(created);
    }

    public CourseDTO getCourseById(Long id) {
        Course course = courseDAO.findById(id);
        if (course == null) {
            throw new NotFoundException("Course not found with id: " + id);
        }
        return new CourseDTO(course);
    }

    public List<CourseDTO> getAllCourses() {
        return courseDAO.findAll().stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }

    public List<CourseDTO> getCoursesByLanguage(String language) {
        return courseDAO.findByLanguage(language).stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }

    public List<CourseDTO> getCoursesByDifficulty(String difficulty) {
        return courseDAO.findByDifficulty(difficulty).stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }

    public CourseDTO updateCourse(Long id, String name, String programmingLanguage, String description, String difficulty) {
        Course course = courseDAO.findById(id);
        if (course == null) {
            throw new NotFoundException("Course not found with id: " + id);
        }
        if (name != null) {
            course.setName(name);
        }
        if (programmingLanguage != null) {
            course.setProgrammingLanguage(programmingLanguage);
        }
        if (description != null) {
            course.setDescription(description);
        }
        if (difficulty != null) {
            course.setDifficulty(Course.Difficulty.valueOf(difficulty.toUpperCase()));
        }
        Course updated = courseDAO.update(course);
        return new CourseDTO(updated);
    }

    public void deleteCourse(Long id) {
        Course course = courseDAO.findById(id);
        if (course == null) {
            throw new NotFoundException("Course not found with id: " + id);
        }
        courseDAO.delete(id);
    }
}
