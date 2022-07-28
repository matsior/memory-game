import exception.NoSuchOptionException;
import io.DataReader;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.InputMismatchException;

public class GameLogic {

    private final WordsManager wordsManager;
    private final DataReader dataReader;

    public GameLogic(WordsManager wordsManager, DataReader dataReader) {
        this.wordsManager = wordsManager;
        this.dataReader = dataReader;
    }

    public void newGame() {
        System.out.println("Choose difficulty level:");
        DifficultyLevel.printOptions();
        DifficultyLevel difficultyLevel = getDifficultyLevel();

        String[] randomWords = wordsManager.getRandomWords(difficultyLevel.getWordsToGuess());
        String[][] gameBoardSolved = wordsManager.putWordsInArray(randomWords);
        String[][] gameplayBoard = new String[difficultyLevel.getWordsToGuess() == 4 ? 2 : 4][4];
        for (String[] rows : gameplayBoard) {
            Arrays.fill(rows, "\u26F6");
        }

        int guessChancesLeft = difficultyLevel.getGuessChances();
        int pairsToGuess = difficultyLevel.getWordsToGuess();

        Instant startTime = Instant.now();
        do {
            System.out.println("Guess chances: " + guessChancesLeft);

            printGameBoard(gameplayBoard);
            int firstWordRow = dataReader.getInt();
            int firstWordColumn = dataReader.getInt();
            String firstWord = checkField(firstWordRow, firstWordColumn, gameplayBoard, gameBoardSolved);

            printGameBoard(gameplayBoard);
            int secondWordRow = dataReader.getInt();
            int secondWordColumn = dataReader.getInt();
            String secondWord = checkField(secondWordRow, secondWordColumn, gameplayBoard, gameBoardSolved);
            printGameBoard(gameplayBoard);

            if (firstWord.equals(secondWord) && (firstWordRow != secondWordRow || firstWordColumn != secondWordColumn)) {
                System.out.println("Gratulations, You find a pair!");
                pairsToGuess--;
            } else if (guessChancesLeft != 0) {
                System.out.println("Try again :(");
                resetFields(firstWordRow, firstWordColumn, secondWordRow, secondWordColumn, gameplayBoard);
            }

            guessChancesLeft--;

            if (pairsToGuess == 0) {
                Instant endTime = Instant.now();
                long gameplayTime = Duration.between(startTime, endTime).toSeconds();
                int chancesUsed = difficultyLevel.getGuessChances() - guessChancesLeft;
                System.out.printf("You solved the memory game after %d chances. It took you %d seconds\n", chancesUsed, gameplayTime);
                break;
            }

            if (guessChancesLeft == 0) {
                System.out.println("You loose :(");
                break;
            }
        } while (true);
    }

    private DifficultyLevel getDifficultyLevel() {
        boolean optionOk = false;
        DifficultyLevel option = null;
        while (!optionOk) {
            try {
                int i = dataReader.getInt();
                option = DifficultyLevel.createFromInt(i);
                optionOk = true;
            } catch (NoSuchOptionException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Its not a number");
            }
        }
        return option;
    }

    private void resetFields(int firstWordRow, int firstWordColumn, int secondWordRow, int secondWordColumn, String[][] gameplayBoard) {
        gameplayBoard[firstWordRow][firstWordColumn] = "X";
        gameplayBoard[secondWordRow][secondWordColumn] = "X";
    }

    private String checkField(int row, int column, String[][] gameplayBoard, String[][] gameBoardSolved) {
        gameplayBoard[row][column] = gameBoardSolved[row][column];
        return gameBoardSolved[row][column];
    }

    private void printGameBoard(String[][] gameBoard) {
        for (int row = 0; row < gameBoard.length; row++) {
            for (int column = 0; column < gameBoard[row].length; column++) {
                if (column == 0) {
                    System.out.print("| ");
                }
                if (column == 3) {
                    System.out.println(gameBoard[row][column] + " | ");
                } else {
                    System.out.print(gameBoard[row][column] + " | ");
                }
            }
        }
    }
}
