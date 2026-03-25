package szachy;


import org.junit.jupiter.api.*;

class PlayerTest {

    @Test
    void testAddPoints() {
        Player p = new Player("X");
        p.addPoints(1.5);
        Assertions.assertEquals(1.5, p.getPoints());
    }

    @Test
    void testName() {
        Player p = new Player("Y");
        Assertions.assertEquals("Y", p.getName());
    }
}