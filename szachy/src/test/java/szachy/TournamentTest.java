package szachy;

import org.junit.jupiter.api.*;

import java.util.*;

class TournamentTest {

    @Test
    void testPairingNoRepeat() {
        List<Player> players = Arrays.asList(
                new Player("A", 2000),
                new Player("B", 1900),
                new Player("C", 1800),
                new Player("D", 1700)
        );
        Tournament t = new Tournament(players);

        Set<Set<String>> allPairs = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            List<Match> matches = t.pairPlayers();
            for (Match m : matches) {
                Set<String> pair = new HashSet<>(Arrays.asList(m.getPlayer1().getName(), m.getPlayer2().getName()));
                Assertions.assertFalse(allPairs.contains(pair), "Pair repeated: " + pair);
                allPairs.add(pair);
                m.setResult(0);
            }
        }
    }

    @Test
    void testDrawsAndPoints() {
        Player a = new Player("A", 2000);
        Player b = new Player("B", 1900);
        Match m = new Match(a, b);
        m.setResult(0);
        Assertions.assertEquals(0.5, a.getPoints());
        Assertions.assertEquals(0.5, b.getPoints());
    }

    @Test
    void testMinimalPlayers() {
        List<Player> players = Arrays.asList(new Player("A", 2000), new Player("B", 1900));
        Tournament t = new Tournament(players);
        List<Match> matches = t.pairPlayers();
        Assertions.assertEquals(1, matches.size());
    }

    @Test
    void testMaximalPairings() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            players.add(new Player("P" + i, 2000 - i));
        }
        Tournament t = new Tournament(players);
        List<Match> matches = t.pairPlayers();
        Assertions.assertTrue(matches.size() == 50 || matches.size() == 49,
                "Should pair all or all but one (bye)");
    }

    @Test
    void testStandingsOrderByPointsThenStartNumber() {
        Player a = new Player("A", 2200);
        Player b = new Player("B", 2000);
        Tournament t = new Tournament(Arrays.asList(a, b));

        a.addPoints(1);
        b.addPoints(1);

        List<Player> sorted = t.getPlayersSorted();
        Assertions.assertEquals("A", sorted.get(0).getName());
    }

    @Test
    void testCanEditRoundResultThroughTournament() {
        Player a = new Player("A", 2200);
        Player b = new Player("B", 2000);
        Tournament t = new Tournament(Arrays.asList(a, b));
        List<Match> matches = t.pairPlayers();

        matches.get(0).setResult(1);
        Assertions.assertTrue(t.updateMatchResult(0, 0, -1));

        Assertions.assertEquals(0, a.getPoints());
        Assertions.assertEquals(1, b.getPoints());
    }
}
