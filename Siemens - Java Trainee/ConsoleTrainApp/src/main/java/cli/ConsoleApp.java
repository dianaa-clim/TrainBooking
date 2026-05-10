package cli;

public class ConsoleApp {
    private final InputReader inputReader;
    private final ConsolePrinter printer;
    private final AuthMenu authMenu;

    public ConsoleApp() {
        this.inputReader = new InputReader();
        this.printer = new ConsolePrinter();
        this.authMenu = new AuthMenu(inputReader, printer);
    }

    public void start() {
        authMenu.show();
    }
}