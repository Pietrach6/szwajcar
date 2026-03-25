package szachy;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private DefaultListModel<String> playerListModel = new DefaultListModel<>();
    private JList<String> playerList = new JList<>(playerListModel);
    private JTextField playerNameField = new JTextField(15);
    private JTextArea outputArea = new JTextArea(15, 30);
    private Tournament tournament;

    public MainFrame() {
        setTitle("Chess Swiss Tournament");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Player name:"));
        inputPanel.add(playerNameField);
        JButton addButton = new JButton("Add Player");
        inputPanel.add(addButton);

        addButton.addActionListener(e -> {
            String name = playerNameField.getText().trim();
            if (!name.isEmpty() && !playerListModel.contains(name)) {
                playerListModel.addElement(name);
                playerNameField.setText("");
            }
        });

        JButton startButton = new JButton("Start Tournament");
        inputPanel.add(startButton);

        startButton.addActionListener(e -> startTournament());

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(playerList), BorderLayout.WEST);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startTournament() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playerListModel.size(); i++) {
            players.add(new Player(playerListModel.get(i)));
        }
        if (players.size() < 2) {
            JOptionPane.showMessageDialog(this, "Add at least 2 players.");
            return;
        }
        tournament = new Tournament(players);
        runRounds();
    }

    private void runRounds() {
        String roundsStr = JOptionPane.showInputDialog(this, "Number of rounds:", "3");
        int rounds = 3;
        try {
            rounds = Integer.parseInt(roundsStr);
        } catch (Exception ignored) {}
        outputArea.setText("");
        for (int round = 1; round <= rounds; round++) {
            outputArea.append("Round " + round + ":\n");
            List<Match> matches = tournament.pairPlayers();
            for (Match match : matches) {
                String[] options = {"Player 1 wins", "Draw", "Player 2 wins"};
                int result = JOptionPane.showOptionDialog(this,
                        match.toString(),
                        "Enter result",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
                match.setResult(result == 0 ? 1 : result == 2 ? -1 : 0); 
                outputArea.append(match + " -> " +
                        (result == 0 ? match.getPlayer1().getName() : result == 2 ? match.getPlayer2().getName() : "Draw") + "\n");
            }
            outputArea.append("Standings:\n");
            for (Player p : tournament.getPlayersSorted()) {
                outputArea.append(p.getName() + ": " + p.getPoints() + "\n");
            }
            outputArea.append("\n");
        }
    }
}

