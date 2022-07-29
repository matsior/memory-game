package io;

import exception.DataImportException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvFileManager {

    private static final String WORDS_FILE_NAME = "Words.txt";
    private static final String HIGH_SCORES_FILE_NAME = "High_scores.txt";

    public List<String> importWords() {
        try {
            return Files.readAllLines(Path.of(WORDS_FILE_NAME));
        } catch (IOException e) {
            throw new DataImportException("File " + WORDS_FILE_NAME + " not found.");
        }
    }
}
