package assignment_2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

import static java.awt.Color.*;


/**
 * Created by Intoleravel on 05/12/2016.
 */
class Structure extends JComponent{

    private Color[][] grid; // grid of colors
    private Shape shapeStart;
    private Shape shapeDestination = new Shape(5,0);
    private Shape offSetPos = new Shape(13,2);
    private Shape shape = new Shape(0,0);
    private int size = 25;
    public boolean run = true;

    // Creates the grid in Shape class and sets up a shape to be dropped
    void start() {
        grid = shape.createGrid();
        if (run) {
            nextShape();
        }
    }

    // Next shape to be dropped
    private void nextShape() {
        //Rotation of the shape to be dropped will always be its first possible position
        shape.rotation = 0;
        shapeStart = new Shape(5, 0); // Shape X and Y is positioned at the top center
        shapeDestination = new Shape(5,0); // Initialise shapeDestination
        ArrayList<Integer> repeat = new ArrayList<>();


        // Add random shapes to nextShapes list
        if(shape.nextShapes.size() < 1) {
            //Collections.addAll(shape.nextShapes, 0, 0, 0);
            Collections.addAll(shape.nextShapes, 0, 1, 2, 3, 4, 5, 6);
            Collections.shuffle(shape.nextShapes);
        }
        Collections.addAll(repeat, 0, 1, 2, 3, 4, 5, 6);
        Collections.shuffle(repeat);

        // Shape to be dropped is the first element of nextShapes assigned as active shape and removed
        // from the nextShapes
        // Add a new shape to the end of nextShapes
        // The fist shape in nextShapes is now assigned as the next shape

        shape.active = shape.nextShapes.get(0);
        shape.nextShapes.remove(0);
        shape.next = shape.nextShapes.get(0);
        shape.nextShapes.add(repeat.get(0));
        System.out.println(shape.nextShapes.toString());

        // shapeDestination X and Y coordinates are assigned
        dropShadow(0);
    }

    // Drops the shape if the game is still running
    // If the shape cannot be dropped it will be placed in the grid of colors
    void moveDown() {
        if(run) {
            if (!checkCollision(shapeStart.x, shapeStart.y + 1)) {
                shapeStart.y = shapeStart.y + 1;

            } else {
                place();
            }

            repaint();
        }
    }


    // Move the shape and its destination from side to side
    // Shape will be move i = 1 to the right and i = -1 to the left
    void moveSide(int i) {
        if(!checkCollision(shapeStart.x + i, shapeStart.y)) {
            shapeStart.x = shapeStart.x + i;
            shapeDestination.x = shapeStart.x;
        }
/*
        if (checkCollision(shapeDestination.x + i, shapeDestination.x + i)) {
            System.out.println("Collides");
            dropShadow(0);
        }*/
        repaint();

    }


    // Check if the active shape collides on its X and Y coordinates with any other color that is not black
    private boolean checkCollision(int x, int y) {
         try {
             for (Block p : shape.shapes[shape.active][shape.rotation]) {
                 if (grid[p.x + x][p.y + y] != Color.BLACK) {
                     return true;
                 }
             }
        }
         catch (ArrayIndexOutOfBoundsException e){
             nextShape();
         }
        return false;
    }

    // Rotate the shape 90 degrees
    void rotate(int i) {
        int check = shape.rotation;
        int nextRotation = (shape.rotation + i) % 4;
        shape.rotation = nextRotation;

        if (!checkCollision(shapeStart.x, shapeStart.y)) {
            shape.rotation = nextRotation;
        }
        else{
            shape.rotation = check;
        }
    }

    // Check if game is over
    boolean gameOver(){
        if (checkCollision(shapeStart.x, shapeStart.y)) // Collision at the top of the grid
        {
            run = false;
            start();
            System.out.println("GameOver");
            return true;
        }
        return false;
    }


    //Drop shape straight to its destination
    void drop(){
        while (!checkCollision(shapeStart.x, shapeStart.y + 1)) {
            shapeStart.y = shapeStart.y + 1;
        }
    }



    // Drop the shape shadow
    void dropShadow(int i) {
        if(run) {
            if(i == 0){
                shapeDestination.y = shapeStart.y + 1;
            }
            while(!checkCollision(shapeDestination.x, shapeDestination.y + 1)) {
                shapeDestination.y = shapeDestination.y + 1;
            }
        }
    }

    // Place shapes in the grid of colors
    private void place() {
        for (int i = 1; i < 11; i++) {
                for (Block p : shape.shapes[shape.active][shape.rotation]) {
                    grid[ p.x + shapeStart.x ][p.y + shapeStart.y ] = shape.colors[shape.active];
            }
        }
        // Check if there is are any rows that do not contain black
        shape.clear(grid);
        // If the game is still running drop the next shape
        if(run){
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
                    g.fill3DRect(size * i, size * j, 25, 25, true);
                }
            }
        }
        g.setColor(darkGray);

        // Paint the active shapes if the game is still running
        if(run) {
            shape.drawShape(g, shapeStart);
            shape.drawNext(g, offSetPos);
            shape.drawPath(g,shapeDestination);
        }
        // If game over paint the total score
        else {
            g.setColor(white);
            g.drawString("GAME OVER", 110,225);
            g.drawString("TOTAL SCORE: "+ shape.score,100,245);
        }
    }



    public Dimension getPreferredSize() {
        return new Dimension(470,565);
    }


}

