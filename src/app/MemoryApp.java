package app;

public class MemoryApp {

    public static void main(String[] args) {

        final String WELCOME_MESSAGE = "Welcome to Memory app.Game :)";

        System.out.println(WELCOME_MESSAGE);
        MemoryApplicationControl memoryApplicationControl = new MemoryApplicationControl();
        memoryApplicationControl.controlLoop();
    }
}
