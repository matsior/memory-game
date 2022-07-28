import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        final String WELCOME_MESSAGE = "Welcome to Memory Game :)";

        System.out.println(WELCOME_MESSAGE);
        MemoryApplicationControl memoryApplicationControl = new MemoryApplicationControl();
        memoryApplicationControl.controlLoop();
    }
}
