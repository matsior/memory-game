package io;

import model.HighScore;
import java.util.List;

public class ConsolePrinter {
    public void printHighScores(List<HighScore> highScores) {
        System.out.println("High Scores");
        System.out.println("[Name] [Date] [Time] [Tries]");
        highScores.forEach(System.out::println);
    }
}
