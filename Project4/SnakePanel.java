package Project4;

import java.awt.*;
import javax.swing.*;

public class SnakePanel extends JPanel {
    private SnakeGame game;
    private final int cellSize = 20;

    public SnakePanel(SnakeGame game) {
        this.game = game;
        int panelWidth = game.getWidth() * cellSize;
        int panelHeight = game.getHeight() * cellSize;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cellWidth = cellSize;
        int cellHeight = cellSize;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.DARK_GRAY);
        for (int x = 0; x <= game.getWidth(); x++) {
            g.drawLine(x * cellWidth, 0, x * cellWidth, game.getHeight() * cellHeight);
        }
        for (int y = 0; y <= game.getHeight(); y++) {
            g.drawLine(0, y * cellHeight, game.getWidth() * cellWidth, y * cellHeight);
        }

        g.setColor(Color.RED);
        for (Point p : game.getFood()) {
            int cellX = p.x() * cellWidth;
            int cellY = p.y() * cellHeight;
            g.fillRect(cellX, cellY, cellWidth, cellHeight);
        }

        g.setColor(Color.GREEN);
        for (Point p : game.getSnake()) {
            int cellX = p.x() * cellWidth;
            int cellY = p.y() * cellHeight;
            g.fillRect(cellX, cellY, cellWidth, cellHeight);
        }
    }
}

