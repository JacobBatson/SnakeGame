package Project4;

public interface SpawningAlgorithm {
    /**
     * This method will be called every game tick. The game will use the return
     * value to determine where to spawn new food.
     *
     * @param empty     all unoccupied points at which the game could spawn food
     * @param timeAlive number of updates since the game began
     * @return an array of points at which the game will attempt to spawn food
     */
    Point[] update(Point[] empty, int timeAlive);
}
