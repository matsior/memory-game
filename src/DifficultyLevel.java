import exception.NoSuchOptionException;

public enum DifficultyLevel {

    EASY(1, "easy", 4, 10),
    HARD(2, "hard", 8, 15);

    private final int id;
    private final String name;
    private final int wordsToGuess;
    private final int guessChances;

    DifficultyLevel(int id, String name, int wordsToGuess, int guessChances) {
        this.id = id;
        this.name = name;
        this.wordsToGuess = wordsToGuess;
        this.guessChances = guessChances;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWordsToGuess() {
        return wordsToGuess;
    }

    public int getGuessChances() {
        return guessChances;
    }

    static DifficultyLevel createFromInt(int option) {
        try {
            return DifficultyLevel.values()[option - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoSuchOptionException("Brak opcji o ID " + option);
        }
    }

    static void printOptions() {
        for (DifficultyLevel value : DifficultyLevel.values()) {
            System.out.println(value);
        }
    }

    @Override
    public String toString() {
        return String.format("[%d] -> %s (%d words to guess)", id, name, wordsToGuess);
    }
}