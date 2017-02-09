package assignment_2;

import java.awt.*;
import java.util.ArrayList;

import static java.awt.Color.BLACK;

/**
 * Created by Intoleravel on 05/12/2016.
 */
class Block { //block
    protected int y;
    protected int x;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
    }

}

public class Shape{
    int y;
    int x;
    int rotation; // Shape position
    int active; // Current shape being used
    int next; // Next shape to be used
    int size = 25;
    int score = 0;
    Color [][] grid;
    ArrayList<Integer> nextShapes = new ArrayList<>();

    public Shape(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Tetris shapes made of 4 blocks each with X and Y coordinates
    // Each shape has 4 possible positions
    // Each shape starts with the first possible position [0][]
    private Block [][] squareShape = {
            {new Block(0, 0), new Block(0, 1), new Block(1, 0), new Block(1, 1)},
            {new Block(0, 0), new Block(0, 1), new Block(1, 0), new Block(1, 1)},
            {new Block(0, 0), new Block(0, 1), new Block(1, 0), new Block(1, 1)},
            {new Block(0, 0), new Block(0, 1), new Block(1, 0), new Block(1, 1)}
    };

    private Block [][] straightShape = {
            { new Block(0, 0), new Block(1, 0), new Block(2, 0), new Block(3, 0) },
            { new Block(0, 0), new Block(0, 1), new Block(0, 2), new Block(0, 3) },
            { new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(3, 1)},
            { new Block(0, 0), new Block(0, 1), new Block(0, 2), new Block(0, 3) }
    };

    private Block [][] lShape = {
            { new Block(0, 0), new Block(1, 0), new Block(2, 0), new Block(2, 1) },
            { new Block(1, 0), new Block(1, 1), new Block(1, 2), new Block(0, 2) },
            { new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(0, 0) },
            { new Block(0, 0), new Block(0, 1), new Block(0, 2), new Block(1, 0) }
    };
    private Block [][] jShape = {
            { new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(2, 0) },
            { new Block(0, 0), new Block(0, 1), new Block(0, 2), new Block(1, 2) },
            { new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(0, 2) },
            { new Block(1, 0), new Block(1, 1), new Block(1, 2), new Block(0, 0) }
    };
    private Block [][] tShape = {
            { new Block(1, 0), new Block(0, 1), new Block(1, 1), new Block(2, 1) },
            { new Block(1, 0), new Block(0, 1), new Block(1, 1), new Block(1, 2) },
            { new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(1, 2) },
            { new Block(0, 0), new Block(0, 1), new Block(1, 1), new Block(0, 2) }
    };
    private Block [][] sShape = {
            { new Block(1, 0), new Block(2, 0), new Block(0, 1), new Block(1, 1) },
            { new Block(0, 0), new Block(0, 1), new Block(1, 1), new Block(1, 2) },
            { new Block(1, 0), new Block(2, 0), new Block(0, 1), new Block(1, 1) },
            { new Block(0, 0), new Block(0, 1), new Block(1, 1), new Block(1, 2) }
    };
    private Block [][] zShape = {
            { new Block(0, 0), new Block(1, 0), new Block(1, 1), new Block(2, 1) },
            { new Block(1, 0), new Block(0, 1), new Block(1, 1), new Block(0, 2) },
            { new Block(0, 0), new Block(1, 0), new Block(1, 1), new Block(2, 1) },
            { new Block(1, 0), new Block(0, 1), new Block(1, 1), new Block(0, 2) }
    };

    //3D Array composed of Tetris shapes
    Block[][][] shapes = {squareShape,straightShape,lShape,jShape,tShape,sShape,zShape};

    // Each color corresponds in colors[] corresponds to a shape in the same index in shapes[index][][]
    Color[] colors = {Color.yellow, Color.cyan, Color.blue, Color.orange, Color.green, Color.pink, Color.red};

    // Create a 2D array of colors
    // [0][..] and [12][..] will serve as borders for the grid so they do not get a color
    // Color BLACK is attributed to other elements in array
    Color [][] createGrid(){
        grid = new Color[12][21];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 20; j++) {
                if (i > 0 && i < 11) {
                    grid[i][j] = BLACK;
                }
            }
        }
        return grid;
    }

    // Draws the shape that is currently active
    // The shape is drawn with a given color(must not be black)
    // The shape is drawn with a given X and Y coordinate
    void drawShape(Graphics g, Shape shapeStart) {
        g.setColor(colors[active]); // Sets the active shape's color
        for (Block p : shapes[active][rotation]) { // For each block of the shape in [rotation] = 1 of 4 positions
            // Draw block in X and Y coordinates
            g.fill3DRect((p.x + shapeStart.x) * size, (p.y + shapeStart.y) * size, size, size, true);
        }


    }
    // Draws the destination of the active shape with given X and Y coordiante
    void drawPath(Graphics g, Shape shapeDestination) {
        g.setColor(Color.white);
        for (Block p : shapes[active][rotation]) {
            g.fill3DRect((p.x + shapeDestination.x) * size, (p.y + shapeDestination.y) * size, size, size, true);
        }

    }

    // Draws the shape that follows the active shape
    // Shape is drawn offset to the grid of colors
     void drawNext(Graphics g, Shape offSetPos) {

        // Black square background surrounding next active shape
        g.setColor(Color.BLACK);
        g.fill3DRect(300,25,125,100,true);

        // Draws next shape with given color
        g.setColor(colors[next]);
        for (Block p : shapes[next][0]) {
            // Square shape is drawn centered in black background surrounding it
            if(nextShapes.get(0)==0){
                g.fill3DRect((p.x + offSetPos.x)* size + 12, (p.y + offSetPos.y) * size, size, size,true);
            }
            // Straight shape is drawn centered in black background surrounding it
            else if(nextShapes.get(0)==1){
                g.fill3DRect((p.x + offSetPos.x)* size-10, (p.y + offSetPos.y) * size+12, size, size,true);
            }
            // All other shapes excluding the above shapes are drawn centered in the black background surrounding it
            else {
            g.fill3DRect((p.x + offSetPos.x)* size, (p.y + offSetPos.y) * size, size, size,true);}
        }

        // Font Color is set Black
        g.setColor(Color.black);
        // Font is set and text is drawn
        Font fontNs = new Font("Courier New", 1 ,15);
        g.setFont(fontNs);
        g.drawString("Next Shape",320,20);
        Font font = new Font("Courier New", 1 ,20);
        g.setFont(font);
        g.drawString("Score: "+ score,320,300);
    }
    // Checks from bottom of the grid of colors upwards for a row that does not contain black
    void clear(Color [][] grid) {
        boolean hasBlack;

        // Checks the row for a black color
        for (int i = 19; i > 0; i--) {
            hasBlack = false;
            //If black color is found  move to the next one from bottom upwards the grid of colors
            for (int j = 1; j < 11; j++) {
                if (grid[j][i] == Color.BLACK) {
                    hasBlack = true;
                    break;
                }
            }

            // If row does not contain black make all above rows drop down -1
            if (!hasBlack) {
                for (int k = i-1; k > 0; k--) {
                    for (int l = 1; l < 11; l++) {
                        grid[l][k+1] = grid[l][k];
                    }
                }
                i += 1;
                // For each row that is cleared add 10 points to the total score
                score = score + 10;
            }
        }

    }
}







