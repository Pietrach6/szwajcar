package szachy;


public class Match {
    private final Player player1;
    private final Player player2;
    private int result; // 1: player1 wins, 0: draw, -1: player2 wins

    public Match(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.result = 0;
    }

    public void setResult(int result) {
        this.result = result;
        if (result == 1) {
            player1.addPoints(1);
        } else if (result == -1) {
            player2.addPoints(1);
        } else {
            player1.addPoints(0.5);
            player2.addPoints(0.5);
        }
    }

    public String toString() {
        return player1.getName() + " vs " + player2.getName();
    }
    
    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}

