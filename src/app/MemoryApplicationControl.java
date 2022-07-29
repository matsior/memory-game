package app;

import exception.NoSuchOptionException;
import io.ConsolePrinter;
import io.CsvFileManager;
import io.DataReader;
import model.HighScore;
import model.MenuOption;

import java.util.InputMismatchException;
import java.util.List;

public class MemoryApplicationControl {

    private final DataReader dataReader = new DataReader();
    private final CsvFileManager csvFileManager = new CsvFileManager();
    private final ConsolePrinter consolePrinter = new ConsolePrinter();
    private final Game game = new Game(csvFileManager, dataReader, consolePrinter);

    void controlLoop() {
        MenuOption menuOption;
        do {
            MenuOption.printOptions();
            menuOption = getOption();
            switch (menuOption) {
                case EXIT -> exit();
                case NEW_GAME -> startNewGame();
                case HIGH_SCORES -> displayHighScores();
                default -> System.out.println("Wprowadź poprawną opcję");
            }
        } while (menuOption != MenuOption.EXIT);
    }

    private void exit() {
        dataReader.close();
        System.out.println("Closing Memory app.Game. See You again :)");
    }

    private void startNewGame() {
        game.startNewGame();
    }

    private void displayHighScores() {
        List<HighScore> highScores = csvFileManager.importHighScores();
        consolePrinter.printHighScores(highScores);
    }

    private MenuOption getOption() {
        boolean optionOk = false;
        MenuOption menuOption = null;
        while (!optionOk) {
            try {
                menuOption = MenuOption.createFromInt(dataReader.getInt());
                optionOk = true;
            } catch (NoSuchOptionException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("It's not a number! Try again.");
            }
        }
        return menuOption;
    }
}