package szachy;


import org.junit.jupiter.api.*;
import java.util.*;

class TournamentTest {

    @Test
    void testPairingNoRepeat() {
        List<Player> players = Arrays.asList(new Player("A"), new Player("B"), new Player("C"), new Player("D"));
        Tournament t = new Tournament(players);

        Set<Set<String>> allPairs = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            List<Match> matches = t.pairPlayers();
            for (Match m : matches) {
                Set<String> pair = new HashSet<>(Arrays.asList(m.getPlayer1().getName(), m.getPlayer2().getName()));
                Assertions.assertFalse(allPairs.contains(pair), "Pair repeated: " + pair);
                allPairs.add(pair);
            }
        }
    }

    @Test
    void testByeAssignment() {
        List<Player> players = Arrays.asList(new Player("A"), new Player("B"), new Player("C"));
        Tournament t = new Tournament(players);

        Set<String> byePlayers = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            t.pairPlayers();
            for (Player p : players) {
                if (p.getPoints() == 1 && !byePlayers.contains(p.getName())) {
                    byePlayers.add(p.getName());
                }
            }
        }
        Assertions.assertEquals(3, byePlayers.size(), "Each player should get a bye once");
    }

    @Test
    void testDrawsAndPoints() {
        Player a = new Player("A");
        Player b = new Player("B");
        Match m = new Match(a, b);
        m.setResult(0); // draw
        Assertions.assertEquals(0.5, a.getPoints());
        Assertions.assertEquals(0.5, b.getPoints());
    }

    @Test
    void testMinimalPlayers() {
        List<Player> players = Arrays.asList(new Player("A"), new Player("B"));
        Tournament t = new Tournament(players);
        List<Match> matches = t.pairPlayers();
        Assertions.assertEquals(1, matches.size());
    }

    @Test
    void testMaximalPairings() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 100; i++) players.add(new Player("P" + i));
        Tournament t = new Tournament(players);
        List<Match> matches = t.pairPlayers();
        Assertions.assertTrue(matches.size() == 50 || matches.size() == 49, "Should pair all or all but one (bye)");
    }

    @Test
    void testStandingsOrder() {
        Player a = new Player("A");
        Player b = new Player("B");
        a.addPoints(2);
        b.addPoints(1);
        Tournament t = new Tournament(Arrays.asList(a, b));
        List<Player> sorted = t.getPlayersSorted();
        Assertions.assertEquals("A", sorted.get(0).getName());
    }
}

