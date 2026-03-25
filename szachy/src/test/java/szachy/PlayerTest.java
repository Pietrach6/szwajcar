package szachy;

import org.junit.jupiter.api.*;

class PlayerTest {

    @Test
    void testAddPoints() {
        Player p = new Player("X", 1800);
        p.addPoints(1.5);
        Assertions.assertEquals(1.5, p.getPoints());
    }

    @Test
    void testNameAndRanking() {
        Player p = new Player("Y", 1500);
        Assertions.assertEquals("Y", p.getName());
        Assertions.assertEquals(1500, p.getRating());
    }
}
