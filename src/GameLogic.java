import exception.NoSuchOptionException;
import io.CsvFileManager;
import io.DataReader;
import model.DifficultyLevel;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameLogic {

    private final CsvFileManager csvFileManager;
    private final DataReader dataReader;
    private List<String> words;

    public GameLogic(CsvFileManager csvFileManager, DataReader dataReader) {
        this.csvFileManager = csvFileManager;
        this.dataReader = dataReader;
        this.words = csvFileManager.importWords();
    }



    public void newGame() {
        System.out.println("Choose difficulty level:");
        DifficultyLevel.printOptions();
        DifficultyLevel difficultyLevel = getDifficultyLevel();


        String[] randomWords = getRandomWords(difficultyLevel.getWordsToGuess(), words);
        String[][] gameBoardSolved = putWordsInArray(randomWords);
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

    private String[] getRandomWords(int amount, List<String> words) {
        Collections.shuffle(words);
        List<String> randomWordsDoubled = words.stream()
                .limit(amount)
                .flatMap(word -> Stream.of(word, word))
                .collect(Collectors.toList());
        Collections.shuffle(randomWordsDoubled);
        return randomWordsDoubled.toArray(String[]::new);
    }

    private String[][] putWordsInArray(String[] words) {
        String[][] result = new String[words.length == 8 ? 2 : 4][4];
        int counter = 0;
        for (int row = 0; row < result.length; row++) {
            for (int column = 0; column < result[row].length; column++) {
                result[row][column] = words[counter];
                counter++;
            }
        }
        return result;
    }
}
