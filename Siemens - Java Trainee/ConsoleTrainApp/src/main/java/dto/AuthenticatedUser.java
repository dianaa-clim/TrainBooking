package dto;

import model.UserRole;

public class AuthenticatedUser {
    private Long id;
    private String fullName;
    private String email;
    private UserRole role;

    public AuthenticatedUser(Long id, String fullName, String email, UserRole role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }
}