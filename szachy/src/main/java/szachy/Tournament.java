package szachy;


import java.util.*;

public class Tournament {
    private final List<Player> players;
    private final List<List<Match>> rounds;

    public Tournament(List<Player> players) {
        this.players = players;
        this.rounds = new ArrayList<>();
    }

    public void runTournament(int roundsCount) {
        for (int round = 1; round <= roundsCount; round++) {
            System.out.println("Round " + round);
            List<Match> matches = pairPlayers();
            for (Match match : matches) {
                System.out.println(match);
                // Simulate result (random for demo)
                int result = new Random().nextInt(3) - 1;
                match.setResult(result);
            }
            rounds.add(matches);
            printStandings();
        }
    }


 // In Tournament.java

 private final Map<Player, Set<Player>> previousPairings = new HashMap<>();
 private final Set<Player> byePlayers = new HashSet<>();

 List<Match> pairPlayers() {
     players.sort(Comparator.comparingDouble(Player::getPoints).reversed());
     List<Match> matches = new ArrayList<>();
     Set<Player> paired = new HashSet<>();
     List<Player> unpaired = new ArrayList<>(players);

     while (unpaired.size() > 1) {
         Player p1 = unpaired.get(0);
         Player p2 = null;
         for (int i = 1; i < unpaired.size(); i++) {
             Player candidate = unpaired.get(i);
             if (!previousPairings.getOrDefault(p1, new HashSet<>()).contains(candidate)) {
                 p2 = candidate;
                 break;
             }
         }
         if (p2 == null) {
             // If no available opponent, pair with next in list (may repeat pairing)
             p2 = unpaired.get(1);
         }
         matches.add(new Match(p1, p2));
         previousPairings.computeIfAbsent(p1, k -> new HashSet<>()).add(p2);
         previousPairings.computeIfAbsent(p2, k -> new HashSet<>()).add(p1);
         paired.add(p1);
         paired.add(p2);
         unpaired.remove(p2);
         unpaired.remove(p1);
     }

     // Handle bye
     if (!unpaired.isEmpty()) {
         Player byePlayer = null;
         for (Player p : unpaired) {
             if (!byePlayers.contains(p)) {
                 byePlayer = p;
                 break;
             }
         }
         if (byePlayer == null) byePlayer = unpaired.get(0);
         byePlayer.addPoints(1);
         byePlayers.add(byePlayer);
         System.out.println(byePlayer.getName() + " gets a bye");
     }
     return matches;
 }

    private void printStandings() {
        System.out.println("Standings:");
        players.sort(Comparator.comparingDouble(Player::getPoints).reversed());
        for (Player p : players) {
            System.out.println(p.getName() + ": " + p.getPoints());
        }
        System.out.println();
    }
    

public java.util.List<Player> getPlayersSorted() {
    java.util.List<Player> sorted = new java.util.ArrayList<>(players);
    sorted.sort(java.util.Comparator.comparingDouble(Player::getPoints).reversed());
    return sorted;
}

}
