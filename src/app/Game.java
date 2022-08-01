package app;

import exception.NoSuchOptionException;
import io.ConsolePrinter;
import io.CsvFileManager;
import io.DataReader;
import model.DifficultyLevel;
import model.HighScore;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {

    private final CsvFileManager csvFileManager;
    private final DataReader dataReader;
    private final ConsolePrinter consolePrinter;
    private List<String> words;
    private List<HighScore> highScores;

    private static final int COLUMNS_NUMBER = 4;
    private static final String BOARD_ICON = "\u26F6";

    public Game(CsvFileManager csvFileManager, DataReader dataReader, ConsolePrinter consolePrinter) {
        this.csvFileManager = csvFileManager;
        this.dataReader = dataReader;
        this.words = csvFileManager.importWords();
        this.highScores = csvFileManager.importHighScores();
        this.consolePrinter = consolePrinter;
    }

    public void startNewGame() {

        DifficultyLevel difficultyLevel = setDifficultyLevel();

        String[][] playersBoard = preparePlayersBoard(difficultyLevel);

        String[][] solutionBoard = prepareSolutionBoard(difficultyLevel);

        int guessChancesLeft = difficultyLevel.getGuessChances();
        int pairsToGuess = difficultyLevel.getWordsToGuess();
        Instant startTime = Instant.now();

        do {
            System.out.println("Guess chances: " + guessChancesLeft);

            consolePrinter.printBoard(playersBoard);

            System.out.println("Type field to uncover");
            String firstField = usersChoice(difficultyLevel);
            String[] firstFieldSplit = firstField.split("");
            String firstWord = checkField(firstFieldSplit, playersBoard, solutionBoard);

            consolePrinter.printBoard(playersBoard);

            System.out.println("Type field to uncover");
            String secondField = usersChoice(difficultyLevel);
            String[] secondFieldSplit = secondField.split("");
            String secondWord = checkField(secondFieldSplit, playersBoard, solutionBoard);

            consolePrinter.printBoard(playersBoard);

            if (firstWord.equals(secondWord) && !firstField.equals(secondField)) {
                System.out.println("Gratulations, You find a pair!");
                pairsToGuess--;
            } else if (guessChancesLeft != 0) {
                System.out.println("It's not a pair! Try again.");
                resetFields(firstFieldSplit, secondFieldSplit, playersBoard);
            }

            guessChancesLeft--;

            // check win condition
            if (pairsToGuess == 0) {
                Instant endTime = Instant.now();
                long gameplayTime = Duration.between(startTime, endTime).toSeconds();
                int chancesUsed = difficultyLevel.getGuessChances() - guessChancesLeft;
                System.out.printf("You solved the memory game after %d chances. It took you %d seconds\n", chancesUsed, gameplayTime);
                System.out.println("Enter your name");
                String name = dataReader.getString();
                HighScore usersHighScore = new HighScore(name, LocalDate.now(), gameplayTime, chancesUsed);
                highScores.add(usersHighScore);
                consolePrinter.printHighScores(highScores);
                csvFileManager.addHighScore(usersHighScore);
                break;
            }

            // check loose condition
            if (guessChancesLeft == 0) {
                System.out.println("You loose :(");
                break;
            }
        } while (true);
    }

    private String usersChoice(DifficultyLevel difficultyLevel) {
        String field = null;
        boolean flag = false;
        int rowsNumber = difficultyLevel.getWordsToGuess() == COLUMNS_NUMBER ? 2 : 4;
        while (!flag) {
            field = dataReader.getString();
            if (field.matches("[A-Da-d][1-4]")) {
                String[] splitted = field.split("");
                if (Integer.parseInt(splitted[1]) <= rowsNumber) {
                    flag = true;
                }
            }
            if (!flag) {
                System.out.println("Not a valid field, try again.");
            }
        }
        return field;
    }

    private DifficultyLevel setDifficultyLevel() {
        System.out.println("Choose difficulty level:");
        DifficultyLevel.printOptions();
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

    private String[][] preparePlayersBoard(DifficultyLevel difficultyLevel) {
        String[][] gameplayBoard = new String[difficultyLevel.getWordsToGuess() == COLUMNS_NUMBER ? 2 : 4][COLUMNS_NUMBER];
        for (String[] rows : gameplayBoard) {
            Arrays.fill(rows, BOARD_ICON);
        }
        return gameplayBoard;
    }

    private String[][] prepareSolutionBoard(DifficultyLevel difficultyLevel) {
        Collections.shuffle(words);
        List<String> randomWordsDoubled = words.stream()
                .limit(difficultyLevel.getWordsToGuess())
                .flatMap(word -> Stream.of(word, word))
                .collect(Collectors.toList());
        Collections.shuffle(randomWordsDoubled);
        String[] randomWords = randomWordsDoubled.toArray(String[]::new);

        String[][] result = new String[randomWords.length == 8 ? 2 : 4][4];
        int counter = 0;
        for (int row = 0; row < result.length; row++) {
            for (int column = 0; column < result[row].length; column++) {
                result[row][column] = randomWords[counter];
                counter++;
            }
        }
        return result;
    }

    private String checkField(String[] field, String[][] playersBoard, String[][] solutionBoard) {
        int row = Integer.parseInt(field[1]) - 1;
        int column = extractFromLetter(field);
        playersBoard[row][column] = solutionBoard[row][column];
        return solutionBoard[row][column];
    }

    private int extractFromLetter(String[] field) {
        return switch (field[0].toUpperCase()) {
            case "A" -> 0;
            case "B" -> 1;
            case "C" -> 2;
            default -> 3;
        };
    }

    private void resetFields(String[] firstField, String[] secondField, String[][] gameplayBoard) {
        int firstWordRow = Integer.parseInt(firstField[1]) - 1;
        int firstWordColumn = extractFromLetter(firstField);
        int secondWordRow = Integer.parseInt(secondField[1]) - 1;
        int secondWordColumn = extractFromLetter(secondField);
        gameplayBoard[firstWordRow][firstWordColumn] = BOARD_ICON;
        gameplayBoard[secondWordRow][secondWordColumn] = BOARD_ICON;
    }
}