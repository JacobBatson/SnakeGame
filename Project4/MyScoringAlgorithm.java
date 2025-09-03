package Project4;

public class MyScoringAlgorithm implements ScoringAlgorithm {

    @Override
    public int update(int snakeSize, int timeAlive) {
        // Add 1 point for every update to reward staying alive
        return 1;
    }

    @Override
    public int gameOver(int snakeSize, int timeAlive) {
        // Bonus points based on snake size when game ends
        return snakeSize * 5;
    }
}