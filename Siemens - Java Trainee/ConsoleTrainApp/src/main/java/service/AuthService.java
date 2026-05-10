package service;

import dto.AuthenticatedUser;
import dto.LoginRequest;
import dto.RegisterRequest;
import exception.AuthenticationException;
import exception.ValidationException;
import model.User;
import model.UserRole;
import repository.UserRepository;

public class AuthService {
    private static final String DEFAULT_ADMIN_EMAIL = "admin@trainapp.com";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public AuthService() {
        this.userRepository = new UserRepository();
        this.passwordService = new PasswordService();
        ensureDefaultAdminExists();
    }

    public AuthenticatedUser login(LoginRequest request) {
        validateLoginRequest(request);

        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Invalid email or password."));

        if (!user.isActive()) {
            throw new AuthenticationException("This account is inactive.");
        }

        boolean passwordMatches = passwordService.verifyPassword(
                request.getPassword(),
                user.getPasswordHash()
        );

        if (!passwordMatches) {
            throw new AuthenticationException("Invalid email or password.");
        }

        return new AuthenticatedUser(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public AuthenticatedUser registerCustomer(RegisterRequest request) {
        validateRegisterRequest(request);

        String email = request.getEmail().trim().toLowerCase();

        userRepository.findByEmail(email).ifPresent(existing -> {
            throw new ValidationException("An account with this email already exists.");
        });

        User user = new User(
                null,
                request.getFullName().trim(),
                email,
                passwordService.hashPassword(request.getPassword()),
                UserRole.CUSTOMER,
                true,
                null
        );

        userRepository.save(user);

        return new AuthenticatedUser(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole()
        );
    }

    private void ensureDefaultAdminExists() {
        if (userRepository.existsByRole(UserRole.ADMIN)) {
            return;
        }

        User admin = new User(
                null,
                "Default Admin",
                DEFAULT_ADMIN_EMAIL,
                passwordService.hashPassword(DEFAULT_ADMIN_PASSWORD),
                UserRole.ADMIN,
                true,
                null
        );

        userRepository.save(admin);

        System.out.println("Default admin created:");
        System.out.println("Email: " + DEFAULT_ADMIN_EMAIL);
        System.out.println("Password: " + DEFAULT_ADMIN_PASSWORD);
    }

    private void validateLoginRequest(LoginRequest request) {
        if (request == null) {
            throw new ValidationException("Login request cannot be null.");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ValidationException("Email is required.");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ValidationException("Password is required.");
        }
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (request == null) {
            throw new ValidationException("Register request cannot be null.");
        }

        if (request.getFullName() == null || request.getFullName().isBlank()) {
            throw new ValidationException("Full name is required.");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ValidationException("Email is required.");
        }

        if (!request.getEmail().contains("@")) {
            throw new ValidationException("Email is invalid.");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new ValidationException("Password must have at least 6 characters.");
        }
    }
}