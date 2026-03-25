package szachy;

public class Player {
    private final String name;
    private final int rating;
    private int startNumber;
    private double points;

    public Player(String name, int rating) {
        this.name = name;
        this.rating = rating;
        this.points = 0;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    public int getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(int startNumber) {
        this.startNumber = startNumber;
    }

    public double getPoints() {
        return points;
    }

    public void addPoints(double d) {
        this.points += d;
    }

    @Override
    public String toString() {
        if (startNumber > 0) {
            return startNumber + ". " + name + " (ranking: " + rating + ")";
        }
        return name + " (ranking: " + rating + ")";
    }
}
