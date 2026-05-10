package cli;

public class MainMenu {
    private final InputReader inputReader;
    private final ConsolePrinter printer;
    private final CustomerMenu customerMenu;
    private final AdminMenu adminMenu;

    public MainMenu(InputReader inputReader, ConsolePrinter printer) {
        this.inputReader = inputReader;
        this.printer = printer;
        this.customerMenu = new CustomerMenu(inputReader, printer);
        this.adminMenu = new AdminMenu(inputReader, printer);
    }

    public void show() {
        boolean running = true;

        while (running) {
            printer.printHeader("CONSOLE TRAIN BOOKING APP");

            System.out.println("1. Customer menu");
            System.out.println("2. Admin menu");
            System.out.println("0. Exit");

            int option = inputReader.readInt("Choose option: ");

            switch (option) {
                case 1 -> customerMenu.show();
                case 2 -> adminMenu.show();
                case 0 -> {
                    System.out.println("Exiting application...");
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}