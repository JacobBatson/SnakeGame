package Project4;

import java.util.Random;

public class MySpawningAlgorithm implements SpawningAlgorithm {
    private Random random = new Random();

    @Override
    public Point[] update(Point[] empty, int timeAlive) {
        // Only spawn food every 5 time units and if there are empty spots
        if (timeAlive % 5 != 0 || empty.length == 0) {
            return new Point[0];
        }

        // Pick a random empty spot to spawn food
        Point randomSpot = empty[random.nextInt(empty.length)];
        return new Point[] { randomSpot };
    }
}