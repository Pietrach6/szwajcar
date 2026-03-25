package szachy;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private final DefaultListModel<Player> playerListModel = new DefaultListModel<>();
    private final JList<Player> playerList = new JList<>(playerListModel);
    private final JTextField playerNameField = new JTextField(12);
    private final JTextField playerRatingField = new JTextField(6);
    private final JTextArea outputArea = new JTextArea(18, 40);

    private Tournament tournament;
    private JButton editResultsButton;

    public MainFrame() {
        setTitle("Chess Swiss Tournament");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Player name:"));
        inputPanel.add(playerNameField);
        inputPanel.add(new JLabel("Ranking:"));
        inputPanel.add(playerRatingField);

        JButton addButton = new JButton("Add Player");
        inputPanel.add(addButton);
        addButton.addActionListener(e -> addPlayer());

        JButton startButton = new JButton("Start Tournament");
        inputPanel.add(startButton);
        startButton.addActionListener(e -> startTournament());

        editResultsButton = new JButton("Edit Result");
        inputPanel.add(editResultsButton);
        editResultsButton.setEnabled(false);
        editResultsButton.addActionListener(e -> editRoundResult());

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(playerList), BorderLayout.WEST);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addPlayer() {
        String name = playerNameField.getText().trim();
        String ratingStr = playerRatingField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Player name is required.");
            return;
        }

        int rating;
        try {
            rating = Integer.parseInt(ratingStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ranking must be an integer.");
            return;
        }

        for (int i = 0; i < playerListModel.size(); i++) {
            if (playerListModel.get(i).getName().equalsIgnoreCase(name)) {
                JOptionPane.showMessageDialog(this, "Player name must be unique.");
                return;
            }
        }

        playerListModel.addElement(new Player(name, rating));
        playerNameField.setText("");
        playerRatingField.setText("");
    }

    private void startTournament() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playerListModel.size(); i++) {
            Player p = playerListModel.get(i);
            players.add(new Player(p.getName(), p.getRating()));
        }
        if (players.size() < 2) {
            JOptionPane.showMessageDialog(this, "Add at least 2 players.");
            return;
        }

        tournament = new Tournament(players);
        runRounds();
        editResultsButton.setEnabled(true);
    }

    private void runRounds() {
        String roundsStr = JOptionPane.showInputDialog(this, "Number of rounds:", "3");
        int rounds = 3;
        try {
            rounds = Integer.parseInt(roundsStr);
        } catch (Exception ignored) {
        }

        outputArea.setText("");
        outputArea.append("Start numbers (by ranking):\n");
        for (Player p : tournament.getPlayersByStartNumbers()) {
            outputArea.append(p.getStartNumber() + ". " + p.getName() + " (ranking: " + p.getRating() + ")\n");
        }
        outputArea.append("\n");

        for (int round = 1; round <= rounds; round++) {
            List<Match> matches = tournament.pairPlayers();
            for (Match match : matches) {
                int result = askForResult(match);
                match.setResult(result);
            }
        }
        refreshTournamentView();
    }

    private int askForResult(Match match) {
        String[] options = {"Player 1 wins", "Draw", "Player 2 wins"};
        int result = JOptionPane.showOptionDialog(this,
                match.toString(),
                "Enter result",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (result == 0) {
            return 1;
        }
        if (result == 2) {
            return -1;
        }
        return 0;
    }

    private void editRoundResult() {
        if (tournament == null || tournament.getRoundCount() == 0) {
            JOptionPane.showMessageDialog(this, "No rounds to edit yet.");
            return;
        }

        Integer[] rounds = new Integer[tournament.getRoundCount()];
        for (int i = 0; i < rounds.length; i++) {
            rounds[i] = i + 1;
        }
        Integer selectedRound = (Integer) JOptionPane.showInputDialog(
                this,
                "Choose round:",
                "Edit Result",
                JOptionPane.QUESTION_MESSAGE,
                null,
                rounds,
                rounds[0]);

        if (selectedRound == null) {
            return;
        }

        List<Match> roundMatches = tournament.getRoundMatches(selectedRound - 1);
        if (roundMatches.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No matches in selected round.");
            return;
        }

        String[] matchOptions = new String[roundMatches.size()];
        for (int i = 0; i < roundMatches.size(); i++) {
            Match m = roundMatches.get(i);
            matchOptions[i] = (i + 1) + ". " + m + " (" + m.getResultLabel() + ")";
        }

        String selectedMatch = (String) JOptionPane.showInputDialog(
                this,
                "Choose match:",
                "Edit Result",
                JOptionPane.QUESTION_MESSAGE,
                null,
                matchOptions,
                matchOptions[0]);

        if (selectedMatch == null) {
            return;
        }

        int matchIndex = -1;
        for (int i = 0; i < matchOptions.length; i++) {
            if (matchOptions[i].equals(selectedMatch)) {
                matchIndex = i;
                break;
            }
        }

        if (matchIndex == -1) {
            return;
        }

        Match chosenMatch = roundMatches.get(matchIndex);
        int newResult = askForResult(chosenMatch);
        tournament.updateMatchResult(selectedRound - 1, matchIndex, newResult);
        refreshTournamentView();
    }

    private void refreshTournamentView() {
        outputArea.setText("");

        outputArea.append("Start numbers (by ranking):\n");
        for (Player p : tournament.getPlayersByStartNumbers()) {
            outputArea.append(p.getStartNumber() + ". " + p.getName() + " (ranking: " + p.getRating() + ")\n");
        }

        outputArea.append("\nRounds:\n");
        for (int round = 0; round < tournament.getRoundCount(); round++) {
            outputArea.append("Round " + (round + 1) + ":\n");
            List<Match> matches = tournament.getRoundMatches(round);
            for (int m = 0; m < matches.size(); m++) {
                Match match = matches.get(m);
                outputArea.append("  " + (m + 1) + ". " + match + " -> " + match.getResultLabel() + "\n");
            }
        }

        outputArea.append("\nStandings (points, tie-break by start number):\n");
        for (Player p : tournament.getPlayersSorted()) {
            outputArea.append(p.getName() + " [#" + p.getStartNumber() + "]: " + p.getPoints() + "\n");
        }
    }
}
