package szachy;

public class Player {
    private final String name;
    private double points;

    public Player(String name) {
        this.name = name;
        this.points = 0;
    }

    public String getName() {
        return name;
    }

    public double getPoints() {
        return points;
    }

    public void addPoints(double d) {
        this.points += d;
    }
}

