package model;

import exception.NoSuchOptionException;

public enum MenuOption {
    EXIT(0, "Exit"),
    NEW_GAME(1, "New Game"),
    HIGH_SCORES(2, "High Scores");

    private final int id;
    private final String description;

    MenuOption(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public static void printOptions() {
        for (MenuOption value : MenuOption.values()) {
            System.out.println(value);
        }
    }

    public static MenuOption createFromInt(int option) throws NoSuchOptionException {
        try {
            return MenuOption.values()[option];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoSuchOptionException("Option with id " + option + " does not exits.");
        }
    }

    @Override
    public String toString() {
        return String.format("[%d] -> %s", id, description);
    }
}
