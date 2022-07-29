package io;

import model.HighScore;

import java.util.Comparator;
import java.util.List;

public class ConsolePrinter {
    public void printHighScores(List<HighScore> highScores) {
        System.out.println("High Scores (based on shortest time)");
        if (highScores.isEmpty()) {
            System.out.println("High scores list is empty.");
        } else {
            System.out.println("[Name] [Date] [Time] [Tries]");
            highScores.stream()
                    .sorted(Comparator.comparing(HighScore::getGuessingTime))
                    .limit(10)
                    .forEach(System.out::println);
        }
    }

    public void printBoard(String[][] board) {
        System.out.println("- | A | B | C | D |");
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if (column == 0) {
                    System.out.print(row + 1 + " | ");
                }
                if (column == 3) {
                    System.out.println(board[row][column] + " | ");
                } else {
                    System.out.print(board[row][column] + " | ");
                }
            }
        }
    }
}
