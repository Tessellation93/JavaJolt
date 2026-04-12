package dk.javajolt.services;
import dk.javajolt.daos.UserDAO;
import dk.javajolt.dtos.UserDTO;
import dk.javajolt.entities.User;
import dk.javajolt.exceptions.NotFoundException;
import dk.javajolt.security.TokenUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class UserService {
    private final UserDAO userDAO;
    public UserService() {
        this.userDAO = UserDAO.getInstance();
    }
    public Map<String, Object> register(String username, String email, String password) {
        if (username == null || email == null || password == null) {
            throw new IllegalArgumentException("Username, email and password are required");
        }
        if (userDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userDAO.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username already taken");
        }
        User user = new User(username, email, password, false);
        User created = userDAO.create(user);
        userDAO.createRole("USER");
        userDAO.addRole(created.getUsername(), "USER");
        created = userDAO.findByUsername(created.getUsername());
        String token = TokenUtils.generateToken(created);
        return Map.of(
                "token", token,
                "userId", created.getId(),
                "username", created.getUsername(),
                "roles", created.getRolesAsStrings()
        );
    }
    public UserDTO createUser(String username, String email, String password, boolean isAdmin) {
        User user = new User(username, email, password, isAdmin);
        return new UserDTO(userDAO.create(user));
    }
    public UserDTO getUserById(Long id) {
        User user = userDAO.findById(id);
        if (user == null) throw new NotFoundException("User not found with id: " + id);
        return new UserDTO(user);
    }
    public UserDTO getUserByEmail(String email) {
        User user = userDAO.findByEmail(email);
        if (user == null) throw new NotFoundException("User not found with email: " + email);
        return new UserDTO(user);
    }
    public List<UserDTO> getAllUsers() {
        return userDAO.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }
    public UserDTO updateUser(Long id, String username, String email) {
        User user = userDAO.findById(id);
        if (user == null) throw new NotFoundException("User not found with id: " + id);
        if (username != null) user.setUsername(username);
        if (email != null) user.setEmail(email);
        return new UserDTO(userDAO.update(user));
    }
    public void deleteUser(Long id) {
        if (userDAO.findById(id) == null) throw new NotFoundException("User not found with id: " + id);
        userDAO.delete(id);
    }
    public boolean verifyPassword(String email, String password) {
        User user = userDAO.findByEmail(email);
        if (user == null) return false;
        return user.verifyPassword(password);
    }
}
