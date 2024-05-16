import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PuzzleGUI {
    private String letters;
    private String[] solutions;
    private int score;
    private JTextArea guessedWordsArea;
    private JTextArea sortedWordsArea;
    private JLabel scoreLabel;

    public PuzzleGUI(String letters, String[] solutions) {
        this.letters = letters;
        this.solutions = solutions;
        this.score = 0;
    }

    public void startGame() {
        JFrame frame = new JFrame("Word Puzzle Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        leftPanel.add(Box.createVerticalGlue());
        JLabel wordBankLabel = new JLabel("Letters to use:");
        wordBankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(wordBankLabel);
        JLabel lettersLabel = new JLabel(letters);
        lettersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(lettersLabel);
        leftPanel.add(Box.createVerticalGlue());

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        guessedWordsArea = new JTextArea(10, 20);
        guessedWordsArea.setEditable(false);
        JScrollPane guessedWordsScrollPane = new JScrollPane(guessedWordsArea);
        rightPanel.add(new JLabel("Guessed Words:"));
        rightPanel.add(guessedWordsScrollPane);

        sortedWordsArea = new JTextArea(10, 20);
        sortedWordsArea.setEditable(false);
        JScrollPane sortedWordsScrollPane = new JScrollPane(sortedWordsArea);
        rightPanel.add(new JLabel("Sorted Words:"));
        rightPanel.add(sortedWordsScrollPane);

        scoreLabel = new JLabel("Score: " + score);
        rightPanel.add(scoreLabel);

        JButton openFileButton = new JButton("Open File");
        openFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select Input File");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
                fileChooser.setFileFilter(filter);

                int returnVal = fileChooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    processFile(file);
                }
            }
        });
        rightPanel.add(openFileButton);

        frame.add(leftPanel);
        frame.add(rightPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void processFile(File file) {
        TextFileInput in = new TextFileInput(file.getAbsolutePath());
        String letters = readLetters(in);
        String[] solutions = readSolutions(in);
    
        UnsortedWordList unsortedList = new UnsortedWordList();
        SortedWordList sortedList = new SortedWordList();
        for (String solution : solutions) {
            unsortedList.add(new Word(solution));
        }
    
        this.letters = letters;
        this.solutions = solutions;
        this.score = 0;
        updateUI();
    
        startGame(); // Start the game after processing the file
    }
    

    private void updateUI() {
        guessedWordsArea.setText("");
        sortedWordsArea.setText("");
        scoreLabel.setText("Score: " + score);
    }

    private String readLetters(TextFileInput in) {
        return in.readLine();
    }

    private String[] readSolutions(TextFileInput in) {
        List<String> solutionsList = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) {
            solutionsList.add(line);
        }
        return solutionsList.toArray(new String[0]);
    }

    public void addGuessedWord(String word) {
        guessedWordsArea.append(word + "\n");
    }

    public void increaseScore(int pointsToAdd) {
        score += pointsToAdd;
        scoreLabel.setText("Score: " + score);
    }

    public void updateSortedWordsArea(String sortedListContents) {
        sortedWordsArea.setText(sortedListContents);
    }

    public static void main(String[] args) {
        PuzzleGUI puzzleGUI = new PuzzleGUI("", new String[0]);
        puzzleGUI.startGame();
    }
}
