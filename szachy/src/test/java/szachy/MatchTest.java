package szachy;


import org.junit.jupiter.api.*;

class MatchTest {

    @Test
    void testSetResultWin() {
        Player a = new Player("A");
        Player b = new Player("B");
        Match m = new Match(a, b);
        m.setResult(1); // a wins
        Assertions.assertEquals(1, a.getPoints());
        Assertions.assertEquals(0, b.getPoints());
    }

    @Test
    void testSetResultLoss() {
        Player a = new Player("A");
        Player b = new Player("B");
        Match m = new Match(a, b);
        m.setResult(-1); // b wins
        Assertions.assertEquals(0, a.getPoints());
        Assertions.assertEquals(1, b.getPoints());
    }
}

