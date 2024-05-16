import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Frequency {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Assuming PuzzleGUI and InputLouis are available and properly implemented elsewhere
            // No changes required here as per the instructions
            PuzzleGUI puzzleGUI = new PuzzleGUI("", new String[0]);
            puzzleGUI.startGame();
            InputLouis.attachFileOpenListener(puzzleGUI);
        });
    }

    private static void countWordsFromFile(String filePath) {
        Map<String, Integer> wordCounts = new HashMap<>();
        Pattern pattern = Pattern.compile("\\W+");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = pattern.split(line.toLowerCase());
                for (String word : words) {
                    if (!word.isEmpty()) {
                        wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        // Instead of printing the word counts, we will now store them in a SortedWordList
        SortedWordList sortedWordList = new SortedWordList();
        wordCounts.forEach((word, count) -> {
            try {
                sortedWordList.add(new Word(word, count));
            } catch (IllegalWordException e) {
                System.err.println("Illegal word encountered: " + e.getMessage());
            }
        });

        // Now we print the sorted words along with their counts
        WordNode current = sortedWordList.getHead();
        while (current != null) {
            System.out.println(current.getWord().getText() + " - " + current.getWord().getCount());
            current = current.getNext();
        }
    }
}