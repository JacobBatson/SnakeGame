package Project4;

public interface ScoringAlgorithm {
    /**
     * This method will be called every game update and can add points to the
     * player's score.
     *
     * @param snakeSize current size of the snake
     * @param timeAlive number of updates since the game began
     * @return points to add to the score
     */
    int update(int snakeSize, int timeAlive);

    /**
     * This method will be called at the end of the game and can add points to
     * the player's score.
     *
     * @param snakeSize current size of the snake
     * @param timeAlive number of updates since the game began
     * @return points to add to the score
     */
    int gameOver(int snakeSize, int timeAlive);
}
