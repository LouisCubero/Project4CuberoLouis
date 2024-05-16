import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/* Blackboard did not allow for resubmission, so I had to email this.
 * Louis Cubero, Project 2; if anyone's code looks very similar to mine, they plagiarized it.
 *
 * Project 2 touches upon linked lists and is a modification of project 1. This is a Word Game.
 */

// Word class implementation
class Word implements Comparable<Word> {
    private final String value;

    Word(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(Word other) {
        return value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Word other = (Word) obj;
        return value.equals(other.value);
    }
}

// WordNode class
class WordNode {
    Word data;
    WordNode next;

    WordNode(Word word) {
        data = word;
        next = null;
    }
}

// Abstract WordList class
abstract class WordList {
    WordNode head;

    WordList() {
        head = null;
    }

    void append(Word word) {
        WordNode newNode = new WordNode(word);
        if (head == null) {
            head = newNode;
        } else {
            WordNode current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    int size() {
        int count = 0;
        WordNode current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    boolean contains(Word word) {
        WordNode current = head;
        while (current != null) {
            if (current.data.equals(word)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    void remove(Word word) {
        if (head == null) {
            return;
        }
        if (head.data.equals(word)) {
            head = head.next;
            return;
        }
        WordNode current = head;
        while (current.next != null) {
            if (current.next.data.equals(word)) {
                current.next = current.next.next;
                return;
            }
            current = current.next;
        }
    }
}

// UnsortedWordList class
class UnsortedWordList extends WordList {
    UnsortedWordList() {
        super();
    }

    void add(Word word) {
        append(word);
    }
}

// SortedWordList class
class SortedWordList extends WordList {
    SortedWordList() {
        super();
    }

    void add(Word word) {
        WordNode newNode = new WordNode(word);
        if (head == null || word.compareTo(head.data) < 0) {
            // If the list is empty or the new word is less than the head node
            newNode.next = head;
            head = newNode;
        } else {
            WordNode current = head;
            while (current.next != null && word.compareTo(current.next.data) >= 0) {
                // Traverse the list until the next node is greater than the new word
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
    }
}

public class InputLouis {
    /**
     * The main method that allows the user to select an input file, reads the file, creates a puzzle GUI, and starts the game.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Input File");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);

        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            TextFileInput in = new TextFileInput(file.getAbsolutePath());
            String letters = readLetters(in);
            String[] solutions = readSolutions(in);

            UnsortedWordList unsortedList = new UnsortedWordList();
            SortedWordList sortedList = new SortedWordList();
            for (String solution : solutions) {
                unsortedList.add(new Word(solution));
            }

            PuzzleGUI puzzleGUI = new PuzzleGUI(letters, solutions);
            puzzleGUI.startGame();
            playGame(puzzleGUI, letters, unsortedList, sortedList);
        } else {
            System.out.println("No file selected.");
        }
    }

    /**
     * Reads the letters from the input file.
     *
     * @param in The TextFileInput object for reading the file.
     * @return The letters read from the file.
     */
    private static String readLetters(TextFileInput in) {
        return in.readLine();
    }

    /**
     * Reads the solutions from the input file.
     *
     * @param in The TextFileInput object for reading the file.
     * @return The solutions read from the file.
     */
    private static String[] readSolutions(TextFileInput in) {
        List<String> solutionsList = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) {
            solutionsList.add(line);
        }
        return solutionsList.toArray(new String[0]);
    }

    /**
     * Checks if a guess is valid based on the given letters.
     *
     * @param guess   The guess to check.
     * @param letters The letters available for the guess.
     * @return True if the guess is valid, false otherwise.
     */
    private static boolean isValidGuess(String guess, String letters) {
        for (char c : guess.toCharArray()) {
            if (letters.indexOf(c) == -1) {
                // If the guess contains a letter not present in the available letters
                return false;
            }
        }
        return true;
    }

    /**
     * Displays an error message in a dialog box.
     *
     * @param message The message to display.
     */
    private static void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Plays the word puzzle game, prompting the user for guesses and updating the GUI.
     *
     * @param puzzleGUI    The PuzzleGUI object representing the game interface.
     * @param letters      The letters available for the game.
     * @param unsortedList The unsorted list of solutions.
     * @param sortedList   The sorted list to store correctly guessed words.
     */
    private static void playGame(PuzzleGUI puzzleGUI, String letters, UnsortedWordList unsortedList, SortedWordList sortedList) {
        int score = 0;
        String allowedLetters = "latipmo";
        while (score < unsortedList.size()) {
            String input = JOptionPane.showInputDialog("Enter your guess:");
            if (input == null) {
                // If the user clicks the cancel button, exit the game
                System.exit(0);
            } else {
                String lowercaseInput = input.toLowerCase();
                if (!lowercaseInput.contains("l")) {
                    // If the guess doesn't contain the letter 'l'
                    displayErrorMessage("Your guess must contain 'L'");
                } else if (input.length() < 5) {
                    // If the guess is less than 5 letters long
                    displayErrorMessage("Your guess is less than 5 letters long!");
                } else if (!isValidGuess(lowercaseInput, letters)) {
                    // If the guess contains letters not in the given set
                    displayErrorMessage("Your guess contains letters that are not in the given set!");
                } else if (unsortedList.contains(new Word(lowercaseInput))) {
                    // If the guess is in the unsorted list
                    Word guessedWord = new Word(lowercaseInput);
                    unsortedList.remove(guessedWord);
                    sortedList.add(guessedWord);
                    int pointsToAdd = 1;
                    if (containsAllAllowedLetters(lowercaseInput, allowedLetters)) {
                        // If the guess contains all the allowed letters, add extra points
                        pointsToAdd += 2;
                    }
                    score += pointsToAdd;
                    puzzleGUI.addGuessedWord(input);
                    puzzleGUI.increaseScore(pointsToAdd); // Increase the score in the GUI

                    // Update the sorted words area
                    StringBuilder sortedWordsBuilder = new StringBuilder();
                    WordNode current = sortedList.head;
                    while (current != null) {
                        sortedWordsBuilder.append(current.data.toString()).append("\n");
                        current = current.next;
                    }
                    puzzleGUI.updateSortedWordsArea(sortedWordsBuilder.toString());

                    JOptionPane.showMessageDialog(null, "Your guess is correct! You earned " + pointsToAdd + " points.");
                } else {
                    // If the guess is not in the solutions list
                    displayErrorMessage("Your guess is not in the solutions list.");
                }
            }
            if (score == unsortedList.size()) {
                // If the score is equal to the total number of solutions
                JOptionPane.showMessageDialog(null, "Congratulations! You have guessed all possible solutions.");
            }
        }
    }

    /**
     * Checks if the input string contains all the allowed letters.
     *
     * @param input          The input string to check.
     * @param allowedLetters The string containing the allowed letters.
     * @return True if the input contains all the allowed letters, false otherwise.
     */
    private static boolean containsAllAllowedLetters(String input, String allowedLetters) {
        for (char c : allowedLetters.toCharArray()) {
            if (!input.contains(String.valueOf(c))) {
                // If the input doesn't contain a particular allowed letter
                return false;
            }
        }
        return true;
    }
}