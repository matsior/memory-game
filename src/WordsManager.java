import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordsManager {

    private List<String> words;
    private static final String FILE_NAME = "Words.txt";

    public WordsManager() {
        try {
            this.words = Files.readAllLines(Path.of(FILE_NAME));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    String[] getRandomWords(int amount) {
        Collections.shuffle(words);
        List<String> randomWordsDoubled = words.stream()
                .limit(amount)
                .flatMap(word -> Stream.of(word, word))
                .collect(Collectors.toList());
        Collections.shuffle(randomWordsDoubled);
        return randomWordsDoubled.toArray(String[]::new);
    }

    String[][] putWordsInArray(String[] words) {
        String[][] result = new String[words.length == 8 ? 2 : 4][4];
        int counter = 0;
        for (int row = 0; row < result.length; row++) {
            for (int column = 0; column < result[row].length; column++) {
                result[row][column] = words[counter];
                counter++;
            }
        }
        return result;
    }
}
