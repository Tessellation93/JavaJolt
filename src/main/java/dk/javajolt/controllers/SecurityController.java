package dk.javajolt.controllers;
import dk.javajolt.daos.UserDAO;
import dk.javajolt.dtos.UserDTO;
import dk.javajolt.entities.User;
import dk.javajolt.security.TokenUtils;
import dk.javajolt.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Set;
public class SecurityController {
    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);
    private final UserDAO userDAO;
    private final UserService userService;
    public SecurityController() {
        this.userDAO = UserDAO.getInstance();
        this.userService = new UserService();
    }
    public void register(Context ctx) {
        logger.info("POST /api/auth/register");
        UserDTO body = ctx.bodyAsClass(UserDTO.class);
        try {
            ctx.status(201).json(userService.register(
                    body.getUsername(),
                    body.getEmail(),
                    body.getPassword()
            ));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("already")) {
                ctx.status(409).json(Map.of("error", e.getMessage()));
            } else {
                ctx.status(400).json(Map.of("error", e.getMessage()));
            }
        }
    }
    //    public void register(Context ctx) {
//        logger.info("POST /api/auth/register");
//        UserDTO body = ctx.bodyAsClass(UserDTO.class);
//        if (body.getUsername() == null || body.getEmail() == null || body.getPassword() == null) {
//            ctx.status(400).json(Map.of("error", "Username, email and password are required"));
//            return;
//        }
//        if (userDAO.findByEmail(body.getEmail()) != null) {
//            ctx.status(409).json(Map.of("error", "Email already in use"));
//            return;
//        }
//        if (userDAO.findByUsername(body.getUsername()) != null) {
//            ctx.status(409).json(Map.of("error", "Username already taken"));
//            return;
//        }
//        User user = new User(body.getUsername(), body.getEmail(), body.getPassword(), false);
//        User created = userDAO.create(user);
//        userDAO.createRole("USER");
//        userDAO.addRole(created.getUsername(), "USER");
//        created = userDAO.findByUsername(created.getUsername());
//        String token = TokenUtils.generateToken(created);
//        ctx.status(201).json(Map.of(
//                "token", token,
//                "userId", created.getId(),
//                "username", created.getUsername(),
//                "roles", created.getRolesAsStrings()
//        ));
//    }
    public void login(Context ctx) {
        logger.info("POST /api/auth/login");
        UserDTO body = ctx.bodyAsClass(UserDTO.class);
        try {
            ctx.status(200).json(userService.login(
                    body.getEmail(),
                    body.getPassword()
            ));
        } catch (IllegalArgumentException e) {
            ctx.status(401).json(Map.of("error", e.getMessage()));
        }
    }
    //    public void login(Context ctx) {
//        logger.info("POST /api/auth/login");
//        UserDTO body = ctx.bodyAsClass(UserDTO.class);
//        User user = userDAO.findByEmail(body.getEmail());
//        if (user == null || !user.verifyPassword(body.getPassword())) {
//            ctx.status(401).json(Map.of("error", "Invalid email or password"));
//            return;
//        }
//        String token = TokenUtils.generateToken(user);
//        ctx.status(200).json(Map.of(
//                "token", token,
//                "userId", user.getId(),
//                "username", user.getUsername(),
//                "roles", user.getRolesAsStrings()
//        ));
//    }
    public void authenticate(Context ctx) {
        String authHeader = ctx.header("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedResponse("Authentication required");
        }
        try {
            Claims claims = TokenUtils.validateToken(authHeader.substring(7));
            ctx.attribute("userId", claims.get("userId", Long.class));
            ctx.attribute("isAdmin", claims.get("isAdmin", Boolean.class));
            ctx.attribute("roles", claims.get("roles", String.class));
        } catch (JwtException e) {
            throw new UnauthorizedResponse("Invalid or expired token");
        }
    }
    public void authorize(Context ctx, Set<String> requiredRoles) {
        if (requiredRoles == null || requiredRoles.isEmpty()) return;
        String rolesAttr = ctx.attribute("roles");
        if (rolesAttr == null) {
            throw new UnauthorizedResponse("No roles found in token");
        }
        boolean hasRole = requiredRoles.stream().anyMatch(r -> rolesAttr.contains(r));
        if (!hasRole) {
            ctx.status(403).json(Map.of("error", "Forbidden: insufficient role"));
        }
    }
}