package io;

import java.util.Scanner;

public class DataReader {

    private final Scanner scanner = new Scanner(System.in);

    public int getInt() {
        try {
            return scanner.nextInt();
        } finally {
            scanner.nextLine();
        }
    }

    public String getString() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}
