package Snake;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Snake implements ActionListener, KeyListener {
    private static Snake snakeInstance = null;

    private enum Direction{
        UP, DOWN, LEFT, RIGHT
    }

    private final JFrame GAME_FRAME = new JFrame();
    private final JPanel RENDER_PANEL = RenderPanel.getInstance();

    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 400;

    private static final int SCALE = 10;

    private final Timer TIMER = new Timer(20,this);
    private final Random RANDOM_GENERATOR = new Random();

    private Point snakeHead;
    private static Point cookieLocation;

    public static List<Point> snakeParts = new ArrayList<>();

    private Direction direction;
    private int ticks;
    private int tailLength;
    private int score;
    private boolean isSnakeInsideFrame;


    private Snake() {
        GAME_FRAME.add(RenderPanel.getInstance());
        GAME_FRAME.addKeyListener(this);

        GAME_FRAME.setTitle("Snake");
        GAME_FRAME.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        GAME_FRAME.setLocationRelativeTo(null);
        GAME_FRAME.setResizable(false);
        GAME_FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GAME_FRAME.setVisible(true);

        startGame();
    }

    private void startGame(){
        cookieLocation = new Point(20, 20);
        snakeHead = new Point(0, 0);
        snakeParts.clear();
        snakeParts.add(snakeHead);
        direction = Direction.DOWN;
        tailLength = 1;
        ticks = 0;
        score = 0;
        isSnakeInsideFrame = true;

        TIMER.start();
    }

    public static Snake getInstance(){
        if(snakeInstance == null){
            synchronized (Snake.class){
                if(snakeInstance == null){
                    snakeInstance = new Snake();
                }
            }
        }
        return snakeInstance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RENDER_PANEL.repaint();
        ticks++;

        // TODO: uniemożliwić poruszanie się w lewo kiedy poruszamy w prawo

        if (ticks % 10 == 0){
            snakeParts.add( new Point(snakeHead));
            switch (direction) {
                case UP:
                    if (snakeHead.y - 1 < 0) {
                        isSnakeInsideFrame = false;
                    } else {
                        snakeHead.setLocation(snakeHead.x, snakeHead.y - 1);
                    }
                    break;

                case DOWN:
                    if( (snakeHead.y + 1) * SCALE >= FRAME_HEIGHT - 30){ // pasek okna na ubuntu zabiera 30 pixeli z planszy
                        isSnakeInsideFrame = false;
                    } else {
                        snakeHead.setLocation(snakeHead.x, snakeHead.y + 1);
                    }
                    break;

                case LEFT:
                    if( snakeHead.x - 1 < 0){
                        isSnakeInsideFrame = false;
                    } else {
                        snakeHead.setLocation(snakeHead.x - 1, snakeHead.y);
                    }
                    break;

                case RIGHT:
                    if( (snakeHead.x + 1) * SCALE >= FRAME_WIDTH) {
                        isSnakeInsideFrame = false;
                    } else {
                        snakeHead.setLocation(snakeHead.x + 1, snakeHead.y);
                    }
                    break;
            }
            if (snakeParts.size() > tailLength){
                snakeParts.remove(0);
            }


            ticks = 0;

            if(snakeHead.equals(cookieLocation)){
                score += 10;
                tailLength++;
                cookieLocation.setLocation(getNewCookieLocation());
            }
        }

        if (isSnakeInsideFrame == false){
            TIMER.stop();
        }
    }



    @Override
    public void keyPressed(KeyEvent e) {
        int keyID = e.getKeyCode();
        switch (keyID){
            case KeyEvent.VK_A:
                if(direction != Direction.RIGHT) direction = Direction.LEFT;
                break;

            case KeyEvent.VK_D:
                if(direction != Direction.LEFT) direction = Direction.RIGHT;
                break;

            case KeyEvent.VK_W:
                if(direction != Direction.DOWN) direction = Direction.UP;
                break;

            case KeyEvent.VK_S:
                if(direction != Direction.UP) direction = Direction.DOWN;
                break;
            case KeyEvent.VK_SPACE: // restart game
                startGame();
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}


    private Point getNewCookieLocation(){
        // TODO: Nowa pozycja ciastka nie może pokrywać się z wężem
        int newXLocation = RANDOM_GENERATOR.nextInt(FRAME_WIDTH / SCALE);
        int newYLocation = RANDOM_GENERATOR.nextInt((FRAME_HEIGHT-30) / SCALE); // musimy odjąć 30 bo pasek u góry okna zjada 30 pixeli

        System.out.println("Nowa pozycja ciastka: X = " + newXLocation + " Y = " + newYLocation );
        return new Point(newXLocation, newYLocation);
    }

    public static Point getCookieLocation() {
        return cookieLocation;
    }

    public static List<Point> getSnakeParts() {
        return snakeParts;
    }

    public static int getScale() {
        return SCALE;
    }

    public static int getFrameWidth() {
        return FRAME_WIDTH;
    }

    public static int getFrameHeight() {
        return FRAME_HEIGHT;
    }
}
