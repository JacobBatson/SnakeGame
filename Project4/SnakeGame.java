package Project4;
import java.util.ArrayList;

/**
 * Logic for a simple snake game. Needs a GUI to be playable, but that's your
 * job!
 *
 * Note that this class is declared {@code final}, which prevents you from
 * extending it. If you want to modify its behavior, then you'll need to use
 * provided interfaces and the strategy pattern discussed in class.
 *
 * @author Zach Kohlberg
 */
public final class SnakeGame {
    /**
     * Minimum width for the game grid.
     */
    public static final int MIN_WIDTH = 5;
    /**
     * Minimum height for the game grid.
     */
    public static final int MIN_HEIGHT = 5;
    /**
     * Constant representing the direction north (-y).
     */
    public static final int DIR_NORTH = 0;
    /**
     * Constant representing the direction east (+x).
     */
    public static final int DIR_EAST = 1;
    /**
     * Constant representing the direction south (+y).
     */
    public static final int DIR_SOUTH = 2;
    /**
     * Constant representing the direction west (-x).
     */
    public static final int DIR_WEST = 3;

    private int width, height;

    private ArrayList<Point> snake, food, empty;
    private int score, timeAlive;
    private boolean gameOver;

    private int direction;
    private SpawningAlgorithm spawningAlgorithm;
    private ScoringAlgorithm scoringAlgorithm;

    /**
     * Creates a game instance with the given width, height, and default algorithms.
     *
     * @param width
     * @param height
     * @throws IllegalArgumentException if {@code width} or {@code height} are below
     *                                  the minimum
     */
    public SnakeGame(int width, int height) {
        this(
                width,
                height,
                new SpawningAlgorithm() {
                    @Override
                    public Point[] update(Point[] empty, int timeAlive) {
                        // returning an empty array spawns no food
                        return new Point[0];
                    }
                },
                new ScoringAlgorithm() {
                    @Override
                    public int update(int snakeSize, int timeAlive) {
                        // returning zero doesn't change the score
                        return 0;
                    }

                    @Override
                    public int gameOver(int snakeSize, int timeAlive) {
                        // returning zero doesn't change the score
                        return 0;
                    }
                });
    }

    /**
     * Creates a game instance with the given width, height, and algorithms.
     *
     * @param width
     * @param height
     * @param spawningAlgorithm
     * @param scoringAlgorithm
     * @throws IllegalArgumentException if {@code width} or {@code height} are below
     *                                  the minimum
     * @throws NullPointerException     if either algorithm is {@code null}
     */
    public SnakeGame(int width, int height, SpawningAlgorithm spawningAlgorithm, ScoringAlgorithm scoringAlgorithm) {
        if (width < MIN_WIDTH) {
            throw new IllegalArgumentException(
                    String.format("Width too small! Width: %s, Min Width: %s.", width, MIN_WIDTH));
        }

        if (height < MIN_HEIGHT) {
            throw new IllegalArgumentException(
                    String.format("Height too small! Height: %s, Min Height: %s.", height, MIN_HEIGHT));
        }

        this.width = width;
        this.height = height;

        this.snake = new ArrayList<>();
        this.food = new ArrayList<>();
        this.empty = new ArrayList<>();

        // all spaces are initially empty
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                empty.add(new Point(x, y));
            }
        }

        // snake begins with length 3, facing north, with its head in the center of the
        // grid
        int cx = width / 2;
        int cy = height / 2;
        snake.add(new Point(cx, cy));
        snake.add(new Point(cx, cy + 1));
        snake.add(new Point(cx, cy + 2));

        // the points occupied by the snake are no longer empty
        for (Point p : snake) {
            empty.remove(p);
        }

        this.score = 0;
        this.timeAlive = 0;
        this.gameOver = false;

        this.setDirection(DIR_NORTH);
        this.setSpawningAlgorithm(spawningAlgorithm);
        this.setScoringAlgorithm(scoringAlgorithm);
    }

    /**
     * Calling this method will move the snake. Depending on the scoring and
     * spawning algorithms, it may also spawn food and/or increase the score.
     *
     * How often you call this method determines the speed of the game. If you don't
     * know how often to call it, then try four times per second and adjust as
     * needed. You can also change the frequency over time to gradually speed up the
     * game and increase the difficulty.
     */
    public void update() {
        // does nothing if the game has ended
        if (gameOver) {
            return;
        }

        ++timeAlive;

        score += scoringAlgorithm.update(snake.size(), timeAlive);

        for (Point p : spawningAlgorithm.update(getEmpty(), timeAlive)) {
            spawnFood(p);
        }

        advanceSnake();
    }

    private Point nextPoint(Point p, int direction) {
        switch (direction) {
            case DIR_NORTH:
                // north = -y to be consistent with screen coordinates and make drawing easier
                return new Point(p.x(), p.y() - 1);
            case DIR_EAST:
                return new Point(p.x() + 1, p.y());
            case DIR_SOUTH:
                // south = +y to be consistent with screen coordinates and make drawing easier
                return new Point(p.x(), p.y() + 1);
            case DIR_WEST:
                return new Point(p.x() - 1, p.y());
            default:
                throw new IllegalArgumentException("Direction must be DIR_NORTH, DIR_EAST, DIR_SOUTH, or DIR_WEST.");
        }
    }

    private void spawnFood(Point p) {
        if (empty.contains(p)) {
            empty.remove(p);
            food.add(p);
        }
    }

    private void advanceSnake() {
        Point next = nextPoint(snake.get(0), direction);
        if (snake.contains(next) || !inBounds(next)) {
            // we lose if we run into the snake or the edge of the screen
            gameOver = true;
            score += scoringAlgorithm.gameOver(snake.size(), timeAlive);
        } else {
            if (food.contains(next)) {
                // eat food and snake grows to occupy the next point
                food.remove(next);
                snake.add(0, next);
            } else {
                // snake moves into the next point
                empty.remove(next);
                snake.add(0, next);
                // delete end of snake because it moved forward
                Point endOfSnake = snake.removeLast();
                // Use this instead if you're running Java 20 or earlier
                // Point endOfSnake = snake.remove(snake.size() - 1);
                empty.add(endOfSnake);
            }
        }
    }

    /**
     * @return whether the point is in the game's area
     */
    public boolean inBounds(Point p) {
        return p.x() >= 0 && p.x() < width && p.y() >= 0 && p.y() < height;
    }

    /**
     * @return the width of the game's grid
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height of the game's grid
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return an array of points occupied by the snake
     */
    public Point[] getSnake() {
        return snake.toArray(new Point[0]);
    }

    /**
     * @return an array of points occupied by food
     */
    public Point[] getFood() {
        return food.toArray(new Point[0]);
    }

    /**
     * @return an array of points occupied by nothing
     */
    public Point[] getEmpty() {
        return empty.toArray(new Point[0]);
    }

    /**
     * @return the player's current score
     */
    public int getScore() {
        return score;
    }

    /**
     * @return whether the game has ended
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * @return direction the snake is moving
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Changes which direction the snake is moving. Use the DIR_* constants to
     * specify a direction.
     *
     * @param direction the snake's new direction
     * @throws IllegalArgumentException if {@code direction} is not a valid number
     */
    public void setDirection(int direction) {
        if (direction < 0 || direction > 3) {
            throw new IllegalArgumentException("Direction must be DIR_NORTH, DIR_EAST, DIR_SOUTH, or DIR_WEST.");
        }

        // The snake can't turn to face into its own neck
        Point head = snake.get(0);
        Point neck = snake.get(1);
        Point next = nextPoint(head, direction);
        if (next.equals(neck)) {
            return;
        }

        this.direction = direction;
    }

    /**
     * Changes the scoring algorithm used by the game.
     *
     * @param scoringAlgorithm
     * @throws NullPointerException if {@code scoringAlgorithm} is {@code null}
     */
    public void setScoringAlgorithm(ScoringAlgorithm scoringAlgorithm) {
        if (scoringAlgorithm == null) {
            throw new NullPointerException("Scoring algorithm cannot be null!");
        }
        this.scoringAlgorithm = scoringAlgorithm;
    }

    /**
     * Changes the spawning algorithm used by the game.
     *
     * @param spawningAlgorithm
     * @throws NullPointerException if {@code spawningAlgorithm} is {@code null}
     */
    public void setSpawningAlgorithm(SpawningAlgorithm spawningAlgorithm) {
        if (spawningAlgorithm == null) {
            throw new NullPointerException("Spawning algorithm cannot be null!");
        }
        this.spawningAlgorithm = spawningAlgorithm;
    }
}
