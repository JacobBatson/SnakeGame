package Project4;

import java.awt.event.*;
import javax.swing.*;

public class Snake {
    private JFrame frame;
    private SnakePanel panel;
    private SnakeGame game;
    private Timer timer;

    public Snake() {
        game = new SnakeGame(20, 20, new MySpawningAlgorithm(), new MyScoringAlgorithm());

        frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new SnakePanel(game);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        timer = new Timer(250, e -> {
            game.update();
            panel.repaint();
            frame.setTitle("Score: " + game.getScore());
        });
        timer.start();

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
                    game.setDirection(SnakeGame.DIR_NORTH);
                } else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                    game.setDirection(SnakeGame.DIR_EAST);
                } else if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
                    game.setDirection(SnakeGame.DIR_SOUTH);
                } else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                    game.setDirection(SnakeGame.DIR_WEST);
                }
            }
        });

        JPanel buttonPanel = new JPanel();

        JButton pauseButton = new JButton("Pause");
        pauseButton.setFocusable(false);
        pauseButton.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
                pauseButton.setText("Resume");
            } else {
                timer.start();
                pauseButton.setText("Pause");
            }
        });

        JButton helpButton = new JButton("Help");
        helpButton.setFocusable(false);
        helpButton.addActionListener(e -> {
            timer.stop();
            JOptionPane.showMessageDialog(frame,"Controls: \n Use WASD or Arrow Keys to move \n Press 'Pause' to pause/resume and 'Help' for controls");
            if (!timer.isRunning() && pauseButton.getText().equals("Pause")) {
                timer.start();
            }
        });

        buttonPanel.add(pauseButton);
        buttonPanel.add(helpButton);
        frame.add(panel, java.awt.BorderLayout.CENTER);
        frame.add(buttonPanel, java.awt.BorderLayout.SOUTH);
        frame.pack();
    }

    public static void main(String[] args) {
        new Snake();
    }
}

