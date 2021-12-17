package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

// The panel is where the whole game runs. Inside this panel all the drawing/visualization happens
public class Panel extends JPanel implements ActionListener {

    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    // The game is divided into squares. How many squares are in a row and column depends on the height and width of the board
    // and the space that each square takes (UNIT_SIZE)
    static final int UNIT_SIZE = 25;
    // GAME_UNITS represents the number of squares in the game board
    static final  int GAME_UNITS = (WIDTH*HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    // The size of the arrays representing the x and y coordinates of the snake's body equals the number of
    // max squares on the game board
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    // The snake has a head and a tail. bodyParts=head+tail. The game begins with 6 body parts.
    int bodyParts = 6;
    int fruitsEaten;
    int fruitX;
    int fruitY;
    // W=up, S=down, A=left, D=right
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    Panel(){
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setBackground(Color.ORANGE);
        this.setFocusable(true);
        this.addKeyListener(new Controller());
        run();
    }
    public void run(){
        spawnFruit();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running) {
            // It draws lines from point (X1|Y1) to point (X2|Y2) where coordinates correspond to width and height of
            // the squares of the game board since the distance between each line equals UNIT_SIZE
            for (int i = 0; i < x.length; i++) {
                g.drawLine(
                        i * UNIT_SIZE, 0, i * UNIT_SIZE, HEIGHT
                );
            }
            for (int f = 0; f < y.length; f++) {
                g.drawLine(0, f * UNIT_SIZE, WIDTH, f * UNIT_SIZE);
            }

            //Draws a fruit with its coordinates and a size of a one game square
            g.setColor(Color.red);
            g.fillRect(fruitX, fruitY, UNIT_SIZE, UNIT_SIZE);

            //Draws each body part of the snake
            for (int i = 0; i < bodyParts; i++) {
                // Different color for the head
                if (i == 0) {
                    g.setColor(new Color(45, 100, 200));
                } else {
                    g.setColor(Color.blue);
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

//            // Draws the score during the game
//            g.setColor(Color.black);
//            g.setFont(new Font("Arial", Font.ITALIC, 40));
//            FontMetrics metrics = getFontMetrics(g.getFont());
//            g.drawString("SCORE: " + fruitsEaten, (WIDTH - metrics.stringWidth("SCORE: " + fruitsEaten))/2, g.getFont().getSize());
        }else {
            gameOver(g);
        }
    }


    public void spawnFruit(){
        // Gives the fruit a random coordinates in the range of the number of squares(WIDTH/UNIT_SIZE) on the game board
        // so the fruit will be spawned within the map. Put differently: randomly chooses a number between 0 and the
        // max number of squares then it multiplies the random number by the UNIT_SIZE to get a point inside a square
        fruitX = random.nextInt(WIDTH/UNIT_SIZE)*UNIT_SIZE;
        fruitY = random.nextInt(HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }

    // Moving the snake
    public void move(){
        // Iterating the body parts coordinates and setting each coordinate to the value of the one before it.
        // We start at the tail (last index of the array) and stop at the index before 0, which is the head.
        for (int i = bodyParts; i>0; i--){
            x[i] =  x[i-1];
            y[i] = y[i-1];
        }

        // Changing the direction depending on the key input
        switch (direction){
            case 'U':
                // y[0] = y Coordinate of the snake's head
                // decreasing it by one UNIT_SIZE makes the snake move up, along the y-axis, because at the highest point
                // the height increases from top to bottom
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'R':
                // The width increases from left to right
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
        }
    }

    public void checkFruitCollision(){
        if ( x[0] == fruitX && y[0] == fruitY ){
            spawnFruit();
            bodyParts++;
            fruitsEaten++;
        }
    }

    public void checkCollisions(){
        // Check if snake is biting itself. Check if the coordinates of the head equals those of the tail
        for (int i = bodyParts; i > 0; i--){
            if (x[i] == x[0] && y[i] == y[0]){
                running = false;
            }
        }
        // Wall checks
        if (x[0] < 0  || x[0] > WIDTH || y[0] > HEIGHT || y[0] < 0 ) {
            running = false;
        }
    }

    public void gameOver(Graphics g){
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 90));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (WIDTH - metrics.stringWidth("GAME OVER"))/2, HEIGHT/2);

        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        metrics = getFontMetrics(g.getFont());
        g.drawString("SCORE: " + fruitsEaten, (WIDTH - metrics.stringWidth("SCORE: " + fruitsEaten))/2, HEIGHT/2 + 50);

    }

    public class Controller extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            // Handles direction changes by key input
            // Allows the snake to move only in 90 degrees angles and not into itself
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkFruitCollision();
            checkCollisions();
        }
        repaint();
    }
}
