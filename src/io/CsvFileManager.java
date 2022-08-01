package io;

import exception.DataExportException;
import exception.DataImportException;
import model.HighScore;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvFileManager {

    private static final String WORDS_FILE_NAME = "Words.txt";
    private static final String HIGH_SCORES_FILE_NAME = "High_scores.txt";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<String> importWords() {
        return importFile(WORDS_FILE_NAME);
    }

    public List<HighScore> importHighScores() {
        List<HighScore> result = new ArrayList<>();
        List<String> allHighScores = importFile(HIGH_SCORES_FILE_NAME);
        for (String highScore : allHighScores) {
            String[] rowArray = highScore.split(";");
            String name = rowArray[0];
            LocalDate date = LocalDate.parse(rowArray[1], DATE_TIME_FORMATTER);
            long guessingTime = Long.parseLong(rowArray[2]);
            int guessingTries = Integer.parseInt(rowArray[3]);
            HighScore highScoreToSave = new HighScore(name, date, guessingTime, guessingTries);
            result.add(highScoreToSave);
        }
        return result;
    }

    public void addHighScore(HighScore highScore) {
        try (
                FileWriter fileWriter = new FileWriter(HIGH_SCORES_FILE_NAME, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            bufferedWriter.newLine();
            bufferedWriter.write(highScore.toCsvFormat());
        } catch (IOException e) {
            throw new DataExportException("File " + WORDS_FILE_NAME + " not found");
        }
    }

    private List<String> importFile(String fileName) {
        try {
            return Files.readAllLines(Path.of(fileName));
        } catch (IOException e) {
            throw new DataImportException("File " + fileName + " not found.");
        }
    }
}
