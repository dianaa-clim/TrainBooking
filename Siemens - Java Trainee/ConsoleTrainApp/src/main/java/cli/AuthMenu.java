package cli;

import dto.AuthenticatedUser;
import dto.LoginRequest;
import dto.RegisterRequest;
import exception.AppException;
import model.UserRole;
import service.AuthService;

public class AuthMenu {
    private final InputReader inputReader;
    private final ConsolePrinter printer;
    private final AuthService authService;

    public AuthMenu(InputReader inputReader, ConsolePrinter printer) {
        this.inputReader = inputReader;
        this.printer = printer;
        this.authService = new AuthService();
    }

    public void show() {
        boolean running = true;

        while (running) {
            printer.printHeader("AUTHENTICATION");

            System.out.println("1. Login");
            System.out.println("2. Register customer account");
            System.out.println("0. Exit");

            int option = inputReader.readInt("Choose option: ");

            switch (option) {
                case 1 -> login();
                case 2 -> registerCustomer();
                case 0 -> {
                    System.out.println("Exiting application...");
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void login() {
        printer.printHeader("LOGIN");

        String email = inputReader.readString("Email: ");
        String password = inputReader.readString("Password: ");

        try {
            AuthenticatedUser user = authService.login(new LoginRequest(email, password));

            printer.printSuccess("Login successful.");
            System.out.println("Welcome, " + user.getFullName() + "!");
            System.out.println("Role: " + user.getRole());

            openMenuForUser(user);

        } catch (AppException e) {
            printer.printError(e.getMessage());
            inputReader.pressEnterToContinue();
        }
    }

    private void registerCustomer() {
        printer.printHeader("REGISTER CUSTOMER");

        String fullName = inputReader.readString("Full name: ");
        String email = inputReader.readString("Email: ");
        String password = inputReader.readString("Password: ");

        try {
            AuthenticatedUser user = authService.registerCustomer(
                    new RegisterRequest(fullName, email, password)
            );

            printer.printSuccess("Account created successfully.");
            System.out.println("You are now logged in as " + user.getFullName() + ".");

            openMenuForUser(user);

        } catch (AppException e) {
            printer.printError(e.getMessage());
            inputReader.pressEnterToContinue();
        }
    }

    private void openMenuForUser(AuthenticatedUser user) {
        if (user.getRole() == UserRole.ADMIN) {
            AdminMenu adminMenu = new AdminMenu(inputReader, printer);
            adminMenu.show();
        } else if (user.getRole() == UserRole.CUSTOMER) {
            CustomerMenu customerMenu = new CustomerMenu(inputReader, printer, user);
            customerMenu.show();
        }
    }
}