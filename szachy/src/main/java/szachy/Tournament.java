package szachy;

import java.util.*;

public class Tournament {
    private final List<Player> players;
    private final List<List<Match>> rounds;

    private final Map<Player, Set<Player>> previousPairings = new HashMap<>();
    private final Set<Player> byePlayers = new HashSet<>();

    public Tournament(List<Player> players) {
        this.players = new ArrayList<>(players);
        this.rounds = new ArrayList<>();
        assignStartNumbers();
    }

    private void assignStartNumbers() {
        List<Player> byRanking = new ArrayList<>(players);
        byRanking.sort(Comparator
                .comparingInt(Player::getRating)
                .reversed()
                .thenComparing(Player::getName));

        for (int i = 0; i < byRanking.size(); i++) {
            byRanking.get(i).setStartNumber(i + 1);
        }
    }

    public List<Match> pairPlayers() {
        players.sort(Comparator.comparingDouble(Player::getPoints).reversed()
                .thenComparingInt(Player::getStartNumber));
        List<Match> matches = new ArrayList<>();
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
                p2 = unpaired.get(1);
            }
            matches.add(new Match(p1, p2));
            previousPairings.computeIfAbsent(p1, k -> new HashSet<>()).add(p2);
            previousPairings.computeIfAbsent(p2, k -> new HashSet<>()).add(p1);
            unpaired.remove(p2);
            unpaired.remove(p1);
        }

        if (!unpaired.isEmpty()) {
            Player byePlayer = null;
            for (Player p : unpaired) {
                if (!byePlayers.contains(p)) {
                    byePlayer = p;
                    break;
                }
            }
            if (byePlayer == null) {
                byePlayer = unpaired.get(0);
            }
            byePlayer.addPoints(1);
            byePlayers.add(byePlayer);
        }

        rounds.add(matches);
        return matches;
    }

    public List<Player> getPlayersSorted() {
        List<Player> sorted = new ArrayList<>(players);
        sorted.sort(Comparator.comparingDouble(Player::getPoints).reversed()
                .thenComparingInt(Player::getStartNumber));
        return sorted;
    }

    public List<Player> getPlayersByStartNumbers() {
        List<Player> sorted = new ArrayList<>(players);
        sorted.sort(Comparator.comparingInt(Player::getStartNumber));
        return sorted;
    }

    public int getRoundCount() {
        return rounds.size();
    }

    public List<Match> getRoundMatches(int roundIndex) {
        if (roundIndex < 0 || roundIndex >= rounds.size()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(rounds.get(roundIndex));
    }

    public boolean updateMatchResult(int roundIndex, int matchIndex, int result) {
        if (roundIndex < 0 || roundIndex >= rounds.size()) {
            return false;
        }
        List<Match> roundMatches = rounds.get(roundIndex);
        if (matchIndex < 0 || matchIndex >= roundMatches.size()) {
            return false;
        }
        roundMatches.get(matchIndex).setResult(result);
        return true;
    }
}
