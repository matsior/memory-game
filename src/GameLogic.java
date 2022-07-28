import exception.NoSuchOptionException;
import io.DataReader;

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
            Arrays.fill(rows, "X");
        }

        int guessChances = difficultyLevel.getGuessChances();
        int pairsToGuess = difficultyLevel.getWordsToGuess();

        do {

            System.out.println("Guess chances: " + guessChances);

            printGameBoard(gameplayBoard);
            int firstWordRow = dataReader.getInt();
            int firstWordColumn = dataReader.getInt();
            String firstWord = checkField(firstWordRow, firstWordColumn, gameplayBoard, gameBoardSolved);

            printGameBoard(gameplayBoard);
            int secondWordRow = dataReader.getInt();
            int secondWordColumn = dataReader.getInt();
            String secondWord = checkField(secondWordRow, secondWordColumn, gameplayBoard, gameBoardSolved);
            printGameBoard(gameplayBoard);

            if (firstWord.equals(secondWord) && firstWordRow != secondWordRow && firstWordColumn != secondWordColumn) {
                System.out.println("Gratulations, You find a pair!");
                pairsToGuess--;
            } else {
                System.out.println("Try again :(");
                resetFields(firstWordRow, firstWordColumn, secondWordRow, secondWordColumn, gameplayBoard);
            }

            guessChances--;

            if (checkLooseCondition(guessChances)) break;
            if (checkWinCondition(pairsToGuess)) break;
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

    private boolean checkWinCondition(int pairsToGuess) {
        if (pairsToGuess == 0) {
            System.out.println("You win!");
            return true;
        }
        return false;
    }

    private boolean checkLooseCondition(int guessChances) {
        if (guessChances == 0) {
            System.out.println("You loose :(");
            return true;
        }
        return false;
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
