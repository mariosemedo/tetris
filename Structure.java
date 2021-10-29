package assignment_2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

import static assignment_2.View.BACKGROUND_COLOR;
import static java.awt.Color.*;

/**
 * Created by Intoleravel on 05/12/2016.
 */
class Structure extends JComponent {

    private Color[][] grid; // grid of colors
    private Shape shapeStart;
    private Shape shapeDestination = new Shape(5, 0);
    private final Shape offSetPos = new Shape(13, 2);
    private final Shape shape = new Shape(0, 0);
    private static final int SIZE = 25;
    public boolean run = true;

    /**
     * Starts the game by creating the grid of colours and setting up the first shape to be dropped.
     */
    void start() {
        grid = shape.createGrid();
        if (run) {
            nextShape();
        }
    }

    /**
     * Prepares the next shape to be dropped.
     */
    private void nextShape() {
        // Rotation of the shape to be dropped will always be its first possible position
        shape.rotation = 0;

        // Shape X and Y is positioned at the top center
        shapeStart = new Shape(5, 0);

        // Initialise shapeDestination
        shapeDestination = new Shape(5, 0);
        ArrayList<Integer> repeat = new ArrayList<>();

        // Add random shapes to nextShapes list
        if (shape.nextShapes.size() < 1) {
            //Collections.addAll(shape.nextShapes, 0, 0, 0);
            Collections.addAll(shape.nextShapes, 0, 1, 2, 3, 4, 5, 6);
            Collections.shuffle(shape.nextShapes);
        }

        Collections.addAll(repeat, 0, 1, 2, 3, 4, 5, 6);
        Collections.shuffle(repeat);

        // Shape to be dropped is the first element of nextShapes assigned as active shape and removed from the nextShapes
        shape.active = shape.nextShapes.get(0);
        shape.nextShapes.remove(0);

        // The fist shape in nextShapes is now assigned as the next shape
        shape.next = shape.nextShapes.get(0);

        // Add a new shape to the end of nextShapes
        shape.nextShapes.add(repeat.get(0));
        System.out.println(shape.nextShapes);

        // shapeDestination X and Y coordinates are assigned
        dropShadow();
    }

    /**
     * Drops the shape if the game is still running.
     * Case the shape cannot be dropped it will be placed in the grid of colors.
     */
    void moveDown() {
        if (!run) return;

        if (!checkCollision(shapeStart.x, shapeStart.y + 1)) {
            shapeStart.y = shapeStart.y + 1;
        } else {
            place();
        }
        repaint();
    }

    /**
     * Moves the shape and its destination from side to side.
     *
     * @param xAxisMove amount of grids the shape can move in the x-axis
     */
    void moveSide(int xAxisMove) {
        if (!checkCollision(shapeStart.x + xAxisMove, shapeStart.y)) {
            shapeStart.x = shapeStart.x + xAxisMove;
            shapeDestination.x = shapeStart.x;
        }

        repaint();
    }

    /**
     * Checks if the active shape collides on its X and Y coordinates with any other color that is not
     * {@link View#BACKGROUND_COLOR}.
     *
     * @param x axis
     * @param y axis
     * @return true if the shape collides, otherwise false.
     */
    private boolean checkCollision(int x, int y) {
        try {
            for (Block p : shape.shapes[shape.active][shape.rotation]) {
                if (grid[p.x + x][p.y + y] != BACKGROUND_COLOR) {
                    return true;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            nextShape();
        }
        return false;
    }

    /**
     * Rotates the shape 90 degrees.
     */
    void rotate() {
        int check = shape.rotation;
        int nextRotation = (shape.rotation + 1) % 4;
        shape.rotation = nextRotation;

        if (!checkCollision(shapeStart.x, shapeStart.y)) {
            shape.rotation = nextRotation;
        } else {
            shape.rotation = check;
        }
    }

    /**
     * Check if game is over.
     *
     * @return true if the game has ended, false otherwise.
     */
    boolean isGameOver() {
        // Collision at the top of the grid
        if (checkCollision(shapeStart.x, shapeStart.y)) {
            run = false;
            start();
            return true;
        }
        return false;
    }

    /**
     * Drop shape straight to its destination.
     */
    void drop() {
        while (!checkCollision(shapeStart.x, shapeStart.y + 1)) {
            shapeStart.y = shapeStart.y + 1;
        }
    }

    /**
     * Positions the drop the shape shadow.
     */
    void dropShadow() {
        if (run) {
            shapeDestination.y = shapeStart.y + 1;

            while (!checkCollision(shapeDestination.x, shapeDestination.y + 1)) {
                shapeDestination.y = shapeDestination.y + 1;
            }
        }
    }

    /**
     * Places in the colour grid.
     */
    private void place() {
        for (int i = 1; i < 11; i++) {
            for (Block p : shape.shapes[shape.active][shape.rotation]) {
                grid[p.x + shapeStart.x][p.y + shapeStart.y] = shape.colors[shape.active];
            }
        }
        // Check if there is are any rows that do not contain BACKGROUND_COLOR
        shape.clear(grid);
        // If the game is still running drop the next shape
        if (run) {
            nextShape();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        // Paint the grid of colors
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 20; j++) {
                g.setColor(grid[i][j]);
                if (i > 0 && i < 11) {
                    g.fill3DRect(SIZE * i, SIZE * j, SIZE, SIZE, true);
                }
            }
        }
        g.setColor(darkGray);

        // Paint the active shapes if the game is still running
        if (run) {
            shape.drawShape(g, shapeStart);
            shape.drawNext(g, offSetPos);
            shape.drawPath(g, shapeDestination);
        }
        // If game over paint the total score
        else {
            g.setColor(white);
            g.drawString("GAME OVER", 110, 225);
            g.drawString("TOTAL SCORE: " + shape.score, 100, 245);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(470, 565);
    }
}

