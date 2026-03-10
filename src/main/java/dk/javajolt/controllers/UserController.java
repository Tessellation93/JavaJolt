package dk.javajolt.controllers;

import dk.javajolt.dtos.UserDTO;
import dk.javajolt.services.UserService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    public void getAll(Context ctx) {
        logger.info("GET /api/users");
        List<UserDTO> users = userService.getAllUsers();
        ctx.json(users).status(200);
    }

    public void getById(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        logger.info("GET /api/users/{}", id);
        ctx.json(userService.getUserById(id)).status(200);
    }

    public void create(Context ctx) {
        logger.info("POST /api/users");
        UserDTO body = ctx.bodyAsClass(UserDTO.class);
        ctx.json(userService.createUser(body.getUsername(), body.getEmail(), body.getPassword(), body.isAdmin())).status(201);
    }

    public void update(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        logger.info("PUT /api/users/{}", id);
        UserDTO body = ctx.bodyAsClass(UserDTO.class);
        ctx.json(userService.updateUser(id, body.getUsername(), body.getEmail())).status(200);
    }

    public void delete(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        logger.info("DELETE /api/users/{}", id);
        userService.deleteUser(id);
        ctx.status(204);
    }

    public void login(Context ctx) {
        logger.info("POST /api/users/login");
        UserDTO body = ctx.bodyAsClass(UserDTO.class);
        ctx.json(userService.login(body.getEmail(), body.getPassword())).status(200);
    }
}