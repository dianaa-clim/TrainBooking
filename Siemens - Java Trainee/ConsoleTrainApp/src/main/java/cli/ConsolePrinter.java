package cli;

public class ConsolePrinter {
    public void printHeader(String title) {
        System.out.println();
        System.out.println("====================================");
        System.out.println(title);
        System.out.println("====================================");
    }

    public void printError(String message) {
        System.out.println();
        System.out.println("ERROR: " + message);
    }

    public void printSuccess(String message) {
        System.out.println();
        System.out.println(message);
    }
}