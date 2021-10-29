package assignment_2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.TimerTask;
import java.util.Timer;
import java.awt.Color;

/**
 * Created by Intoleravel on 05/12/2016.
 */
public class View extends JFrame {

    public static final Color BACKGROUND_COLOR = Color.BLACK;

    public View() {
        JFrame f = new JFrame("Tetris by Mario Oliveira (1502867)");
        Container contentPane = f.getContentPane();
        final Structure game = new Structure();
        contentPane.add(game, BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setSize(470, 565);
        f.setVisible(true);
        f.getContentPane().setBackground(Color.pink);

        // Start the game of Tetris
        game.start();

        // Mouse controls for the shape movement
        f.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (e.getButton()) {
                    // Right mouse button to move shape to the right
                    case KeyEvent.KEY_LOCATION_RIGHT:
                        game.moveSide(1);
                        game.dropShadow();
                        break;
                    // Left mouse button to move shape to the left
                    case KeyEvent.KEY_LOCATION_STANDARD:
                        game.moveSide(-1);
                        game.dropShadow();
                        break;
                    // Middle mouse button to rotate shape 90 degrees
                    case KeyEvent.KEY_LOCATION_LEFT:
                        game.rotate();
                        game.dropShadow();
                        break;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        // Key controls for the shape movement
        f.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    // Arrow up key to rotate shape
                    case KeyEvent.VK_UP:
                        game.rotate();
                        game.dropShadow();
                        break;
                    // Arrow left key to move shape to the left
                    case KeyEvent.VK_LEFT:
                        game.moveSide(-1);
                        game.dropShadow();
                        break;

                    // Arrow right key to move shape to the right
                    case KeyEvent.VK_RIGHT:
                        game.moveSide(+1);
                        game.dropShadow();
                        break;

                    // Space bar to drop the shape to its final destination
                    case KeyEvent.VK_SPACE:
                        game.drop();
                        if (game.isGameOver()) {
                            System.out.println("Game has been terminated.");
                        }
                        break;
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        });
        // Make the shape drop every second if the game is still running
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (game.run) {
                    game.moveDown();
                    if (game.isGameOver()) {
                        System.out.println("Game has been terminated.");
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(task, 100, 1000);
    }

    public static void main(String[] args) {
        new View();
    }
}



