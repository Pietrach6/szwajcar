package szachy;

public class Match {
    private final Player player1;
    private final Player player2;
    private Integer result; // 1: player1 wins, 0: draw, -1: player2 wins

    public Match(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.result = null;
    }

    public void setResult(int newResult) {
        if (result != null) {
            applyDeltaForResult(result, -1);
        }
        this.result = newResult;
        applyDeltaForResult(result, 1);
    }

    private void applyDeltaForResult(int matchResult, int sign) {
        if (matchResult == 1) {
            player1.addPoints(1 * sign);
        } else if (matchResult == -1) {
            player2.addPoints(1 * sign);
        } else {
            player1.addPoints(0.5 * sign);
            player2.addPoints(0.5 * sign);
        }
    }

    public Integer getResult() {
        return result;
    }

    public String getResultLabel() {
        if (result == null) {
            return "Pending";
        }
        if (result == 1) {
            return player1.getName() + " wins";
        }
        if (result == -1) {
            return player2.getName() + " wins";
        }
        return "Draw";
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
