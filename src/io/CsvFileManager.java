package io;

import exception.DataImportException;
import model.HighScore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvFileManager {

    private static final String WORDS_FILE_NAME = "Words.txt";
    private static final String HIGH_SCORES_FILE_NAME = "High_scores.txt";

    public List<String> importWords() {
        return importFile(WORDS_FILE_NAME);
    }

    public List<HighScore> importHighScores() {
        List<HighScore> result = new ArrayList<>();
        List<String> allHighScores = importFile(HIGH_SCORES_FILE_NAME);
        for (String highScore : allHighScores) {
            String[] rowArray = highScore.split(";");
            String name = rowArray[0];
            LocalDate date = LocalDate.of(2001, 1, 28);
            long guessingTime = Long.parseLong(rowArray[2]);
            int guessingTries = Integer.parseInt(rowArray[3]);
            HighScore highScoreToSave = new HighScore(name, date, guessingTime, guessingTries);
            result.add(highScoreToSave);
        }
        return result;
    }

    private List<String> importFile(String fileName) {
        try {
            return Files.readAllLines(Path.of(fileName));
        } catch (IOException e) {
            throw new DataImportException("File " + fileName + " not found.");
        }
    }
}
