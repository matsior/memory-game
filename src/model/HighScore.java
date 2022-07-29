package model;

import java.time.LocalDate;

public class HighScore {
    private final String name;
    private final LocalDate date;
    private final long guessingTime;
    private final int guessingTries;

    public HighScore(String name, LocalDate date, long guessingTime, int guessingTries) {
        this.name = name;
        this.date = date;
        this.guessingTime = guessingTime;
        this.guessingTries = guessingTries;
    }

    @Override
    public String toString() {
        return  String.format("%s | %s | %d | %d", name, date.toString(), guessingTime, guessingTries);
    }
}
