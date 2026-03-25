package szachy;

import org.junit.jupiter.api.*;

class MatchTest {

    @Test
    void testSetResultWin() {
        Player a = new Player("A", 2000);
        Player b = new Player("B", 1900);
        Match m = new Match(a, b);
        m.setResult(1);
        Assertions.assertEquals(1, a.getPoints());
        Assertions.assertEquals(0, b.getPoints());
    }

    @Test
    void testSetResultLoss() {
        Player a = new Player("A", 2000);
        Player b = new Player("B", 1900);
        Match m = new Match(a, b);
        m.setResult(-1);
        Assertions.assertEquals(0, a.getPoints());
        Assertions.assertEquals(1, b.getPoints());
    }

    @Test
    void testResultCanBeEdited() {
        Player a = new Player("A", 2000);
        Player b = new Player("B", 1900);
        Match m = new Match(a, b);
        m.setResult(1);
        m.setResult(0);

        Assertions.assertEquals(0.5, a.getPoints());
        Assertions.assertEquals(0.5, b.getPoints());
    }
}
