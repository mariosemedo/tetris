package assignment_2;

import java.awt.*;
import java.util.ArrayList;

import static assignment_2.View.BACKGROUND_COLOR;

/**
 * Tetris shapes made of 4 blocks each with X and Y coordinates.
 * Each shape has 4 possible positions.
 * Each shape starts with the first possible position [0][]
 */
public class Shape {
    int y;
    int x;
    int rotation; // Shape position
    int active; // Current shape being used
    int next; // Next shape to be used
    int size = 25;
    int score = 0;
    Color[][] grid;
    ArrayList<Integer> nextShapes = new ArrayList<>();

    public Shape(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private final Block[][] squareShape = {
            {new Block(0, 0), new Block(0, 1), new Block(1, 0), new Block(1, 1)},
            {new Block(0, 0), new Block(0, 1), new Block(1, 0), new Block(1, 1)},
            {new Block(0, 0), new Block(0, 1), new Block(1, 0), new Block(1, 1)},
            {new Block(0, 0), new Block(0, 1), new Block(1, 0), new Block(1, 1)}
    };

    private final Block[][] straightShape = {
            {new Block(0, 0), new Block(1, 0), new Block(2, 0), new Block(3, 0)},
            {new Block(0, 0), new Block(0, 1), new Block(0, 2), new Block(0, 3)},
            {new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(3, 1)},
            {new Block(0, 0), new Block(0, 1), new Block(0, 2), new Block(0, 3)}
    };

    private final Block[][] lShape = {
            {new Block(0, 0), new Block(1, 0), new Block(2, 0), new Block(2, 1)},
            {new Block(1, 0), new Block(1, 1), new Block(1, 2), new Block(0, 2)},
            {new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(0, 0)},
            {new Block(0, 0), new Block(0, 1), new Block(0, 2), new Block(1, 0)}
    };
    private final Block[][] jShape = {
            {new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(2, 0)},
            {new Block(0, 0), new Block(0, 1), new Block(0, 2), new Block(1, 2)},
            {new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(0, 2)},
            {new Block(1, 0), new Block(1, 1), new Block(1, 2), new Block(0, 0)}
    };
    private final Block[][] tShape = {
            {new Block(1, 0), new Block(0, 1), new Block(1, 1), new Block(2, 1)},
            {new Block(1, 0), new Block(0, 1), new Block(1, 1), new Block(1, 2)},
            {new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(1, 2)},
            {new Block(0, 0), new Block(0, 1), new Block(1, 1), new Block(0, 2)}
    };
    private final Block[][] sShape = {
            {new Block(1, 0), new Block(2, 0), new Block(0, 1), new Block(1, 1)},
            {new Block(0, 0), new Block(0, 1), new Block(1, 1), new Block(1, 2)},
            {new Block(1, 0), new Block(2, 0), new Block(0, 1), new Block(1, 1)},
            {new Block(0, 0), new Block(0, 1), new Block(1, 1), new Block(1, 2)}
    };
    private final Block[][] zShape = {
            {new Block(0, 0), new Block(1, 0), new Block(1, 1), new Block(2, 1)},
            {new Block(1, 0), new Block(0, 1), new Block(1, 1), new Block(0, 2)},
            {new Block(0, 0), new Block(1, 0), new Block(1, 1), new Block(2, 1)},
            {new Block(1, 0), new Block(0, 1), new Block(1, 1), new Block(0, 2)}
    };

    // 3D Array composed of Tetris shapes
    Block[][][] shapes = {squareShape, straightShape, lShape, jShape, tShape, sShape, zShape};

    // Each color corresponds in colors[] corresponds to a shape in the same index in shapes[index][][]
    Color[] colors = {Color.yellow, Color.cyan, Color.blue, Color.orange, Color.green, Color.pink, Color.red};

    // Create a 2D array of colors
    // [0][..] and [12][..] will serve as borders for the grid, so they do not get a color.
    // The colour BACKGROUND_COLOR is attributed to other elements in array.
    Color[][] createGrid() {
        grid = new Color[12][21];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 20; j++) {
                if (i > 0 && i < 11) {
                    grid[i][j] = BACKGROUND_COLOR;
                }
            }
        }
        return grid;
    }

    /**
     * Draws the shape that is currently active - the shape is drawn with a given X and Y coordinate.
     * <p>
     * The shape is drawn with a given color - must NOT be {@link View#BACKGROUND_COLOR}.
     *
     * @param graphics {@link Graphics}.
     * @param shape    {@link Shape} to be drawn.
     */
    void drawShape(Graphics graphics, Shape shape) {
        graphics.setColor(colors[active]); // Sets the active shape's color
        for (Block p : shapes[active][rotation]) { // For each block of the shape in [rotation] = 1 of 4 positions
            // Draw block in X and Y coordinates
            graphics.fill3DRect((p.x + shape.x) * size, (p.y + shape.y) * size, size, size, true);
        }
    }

    /**
     * Draws the destination of the active shape with given X and Y coordinates
     *
     * @param graphics {@link Graphics}.
     * @param shape    {@link Shape} to be drawn.
     */
    void drawPath(Graphics graphics, Shape shape) {
        graphics.setColor(Color.white);
        for (Block p : shapes[active][rotation]) {
            graphics.fill3DRect((p.x + shape.x) * size, (p.y + shape.y) * size, size, size, true);
        }

    }

    /**
     * Draws the shape that follows the active shape.
     *
     * @param graphics {@link Graphics}.
     * @param shape    {@link Shape} to be drawn.
     */
    void drawNext(Graphics graphics, Shape shape) {

        // BACKGROUND_COLOR square background surrounding next active shape
        graphics.setColor(BACKGROUND_COLOR);
        graphics.fill3DRect(300, 25, 125, 100, true);

        // Draws next shape with given color
        graphics.setColor(colors[next]);
        for (Block p : shapes[next][0]) {
            // Square shape is drawn centered in BACKGROUND_COLOR surrounding it
            if (nextShapes.get(0) == 0) {
                graphics.fill3DRect((p.x + shape.x) * size + 12, (p.y + shape.y) * size, size, size, true);
            }
            // Straight shape is drawn centered in BACKGROUND_COLOR surrounding it
            else if (nextShapes.get(0) == 1) {
                graphics.fill3DRect((p.x + shape.x) * size - 10, (p.y + shape.y) * size + 12, size, size, true);
            }
            // All other shapes excluding the above shapes are drawn centered in the BACKGROUND_COLOR surrounding it
            else {
                graphics.fill3DRect((p.x + shape.x) * size, (p.y + shape.y) * size, size, size, true);
            }
        }

        // Font Color is set BACKGROUND_COLOR
        graphics.setColor(BACKGROUND_COLOR);

        // Font is set and text is drawn
        Font fontNs = new Font("Courier New", Font.BOLD, 15);
        graphics.setFont(fontNs);
        graphics.drawString("Next Shape", 320, 20);
        Font font = new Font("Courier New", Font.BOLD, 20);
        graphics.setFont(font);
        graphics.drawString("Score: " + score, 320, 300);
    }

    //

    /**
     * Clears any horizontal line of colours in the grid that do not contain {@link View#BACKGROUND_COLOR}. This is
     * done by checking from bottom of the grid upwards for a row that does not contain {@link View#BACKGROUND_COLOR}.
     *
     * @param grid of colours.
     */
    void clear(Color[][] grid) {
        boolean hasBackgroundColor;

        // Checks the row for a BACKGROUND_COLOR
        for (int i = 19; i > 0; i--) {
            hasBackgroundColor = false;
            // If BACKGROUND_COLOR is found  move to the next one from bottom upwards the grid of colors
            for (int j = 1; j < 11; j++) {
                if (grid[j][i] == BACKGROUND_COLOR) {
                    hasBackgroundColor = true;
                    break;
                }
            }

            // If row does not contain BACKGROUND_COLOR make all above rows dropdown one position
            if (!hasBackgroundColor) {
                for (int k = i - 1; k > 0; k--) {
                    for (int l = 1; l < 11; l++) {
                        grid[l][k + 1] = grid[l][k];
                    }
                }
                i += 1;
                // For each row that is cleared add 10 points to the total score
                score = score + 10;
            }
        }
    }
}







