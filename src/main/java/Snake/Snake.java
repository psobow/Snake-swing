package Snake;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class Snake implements ActionListener, KeyListener {
    private static Snake snakeInstance = null;

    private enum Direction{
        UP, DOWN, LEFT, RIGHT
    }
    private final int DELAY_IN_MS = 100;
    private final JPanel RENDER_PANEL = RenderPanel.getInstance();
    private final Timer TIMER = new Timer(DELAY_IN_MS,this);
    private final Random RANDOM_GENERATOR = new Random();

    // Those 3 guys have to be static cuz i need them in RenderPanel.paintComponent();
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 400;
    private static final int SCALE = 10;

    private final int INITIAL_TAIL_LENGTH = 1;
    private final int INITIAL_HEAD_X_COORDINATE = 0;
    private final int INITIAL_HEAD_Y_COORDINATE = 0;
    private final int INITIAL_COOKIE_X_COORDINATE = 20;
    private final int INITIAL_COOKIE_Y_COORDINATE = 20;
    private final int PIXEL_AMOUNT_TAKEN_BY_FRAME_BAR = 30;

    private Direction direction;

    // Has to be static package-public cuz i need it in RenderPanel.paintComponent();
    static List<Point> snakeParts = new ArrayList<>();

    private Point snakeHead;

    // static cuz need this in RenderPanel
    private static Point cookie;

    // this is variable on purpose isNewHeadPositionCollidingWithBody()
    private Point bodyCollisionPoint = new Point(0,0);


    private static int snakeLength;

    private int newHeadCoordinateX;
    private int newHeadCoordinateY;

    private static int score;

    // i need this in RenderPanel so static again...
    private static boolean isPossibleToMove;

    private boolean isDirectionChangePossible;


    private Snake() {
        JFrame GAME_FRAME = new JFrame();
        GAME_FRAME.add(RENDER_PANEL);
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
        newHeadCoordinateX = INITIAL_HEAD_X_COORDINATE;
        newHeadCoordinateY = INITIAL_HEAD_Y_COORDINATE;
        snakeHead = new Point(newHeadCoordinateX, newHeadCoordinateY);
        snakeParts.clear();
        snakeParts.add(snakeHead);
        snakeLength = INITIAL_TAIL_LENGTH;
        direction = Direction.DOWN;
        isPossibleToMove = true;
        isDirectionChangePossible = true;

        cookie = new Point(INITIAL_COOKIE_X_COORDINATE, INITIAL_COOKIE_Y_COORDINATE);
        score = 0;

        if (snakeLength < 1 ||
            snakeHead.x < 0 || snakeHead.x >= FRAME_WIDTH / SCALE ||
            snakeHead.y < 0 || snakeHead.y >= (FRAME_HEIGHT - PIXEL_AMOUNT_TAKEN_BY_FRAME_BAR) / SCALE ||
            cookie.x < 0    || cookie.x >= FRAME_WIDTH / SCALE ||
            cookie.y < 0    || cookie.y >= (FRAME_HEIGHT - PIXEL_AMOUNT_TAKEN_BY_FRAME_BAR) / SCALE)
            throw new IllegalArgumentException("Invalid initial values."
            );

    }

    static Snake getInstance(){
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
        RENDER_PANEL.revalidate();
        RENDER_PANEL.repaint();

        if(snakeHead.equals(cookie)){
            score += 10;
            snakeLength++;
            cookie.setLocation(getNewCookieLocation());
        }

        if(isPossibleToMove) {
            switch (direction) {
                case UP:
                    newHeadCoordinateY = snakeHead.y - 1;
                    if (newHeadCoordinateY < 0 || isNewHeadPositionCollidingWithBody()) {
                        isPossibleToMove = false;
                    } else {
                        snakeHead.setLocation(snakeHead.x, newHeadCoordinateY);
                    }
                    break;

                case DOWN:
                    newHeadCoordinateY = snakeHead.y + 1;
                    if (newHeadCoordinateY * SCALE >= FRAME_HEIGHT - PIXEL_AMOUNT_TAKEN_BY_FRAME_BAR
                            || isNewHeadPositionCollidingWithBody()) {
                        isPossibleToMove = false;
                    } else {
                        snakeHead.setLocation(snakeHead.x, newHeadCoordinateY);
                    }
                    break;

                case LEFT:
                    newHeadCoordinateX = snakeHead.x - 1;
                    if (newHeadCoordinateX < 0 || isNewHeadPositionCollidingWithBody()) {
                        isPossibleToMove = false;
                    } else {
                        snakeHead.setLocation(newHeadCoordinateX, snakeHead.y);
                    }
                    break;

                case RIGHT:
                    newHeadCoordinateX = snakeHead.x + 1;
                    if (newHeadCoordinateX * SCALE >= FRAME_WIDTH || isNewHeadPositionCollidingWithBody()) {
                        isPossibleToMove = false;
                    } else {
                        snakeHead.setLocation(newHeadCoordinateX, snakeHead.y);
                    }
                    break;
            }

            // Po zmianie kierunku ruchu węża, zezwól na wprowadzanie kolejnej zmiany.
            isDirectionChangePossible = true;

            /*
            W kolekcji snakeParts pod indexem 0 znajduję się najstarszy element węża.
            W każdym evencie dodajemy nowy element na przód węża (pod największym indexem, znajduje się najnowszy element).
            jeżeli ilość elementów jest większa niż długość węża to usuwamy najstarszy element.
             */
            snakeParts.add( new Point(snakeHead));
            if (snakeParts.size() > snakeLength){
                snakeParts.remove(0);
            }
        }
    }

    private Point getNewCookieLocation(){
        // TODO: Nowa pozycja ciastka nie może pokrywać się z wężem
        int newXLocation = RANDOM_GENERATOR.nextInt((FRAME_WIDTH / SCALE) - 1);
        int newYLocation = RANDOM_GENERATOR.nextInt((FRAME_HEIGHT - PIXEL_AMOUNT_TAKEN_BY_FRAME_BAR) / SCALE);

        //log.info("Nowa pozycja ciastka: X = " + newXLocation + " Y = " + newYLocation );
        return new Point(newXLocation, newYLocation);
    }

    private boolean isNewHeadPositionCollidingWithBody() {
        bodyCollisionPoint.setLocation(newHeadCoordinateX, newHeadCoordinateY);
        boolean result = snakeParts.contains(bodyCollisionPoint);
        if (result) {
            if (bodyCollisionPoint.equals(snakeParts.get(0))) return false;
            else return true;
        }

        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyID = e.getKeyCode();
        switch (keyID) {
            case KeyEvent.VK_A:
                if (direction != Direction.RIGHT && isDirectionChangePossible) {
                    isDirectionChangePossible = false;
                    direction = Direction.LEFT;
                }
                break;

            case KeyEvent.VK_D:
                if (direction != Direction.LEFT && isDirectionChangePossible) {
                    isDirectionChangePossible = false;
                    direction = Direction.RIGHT;
                }
                break;

            case KeyEvent.VK_W:
                if (direction != Direction.DOWN && isDirectionChangePossible) {
                    isDirectionChangePossible = false;
                    direction = Direction.UP;
                }
                break;

            case KeyEvent.VK_S:
                if (direction != Direction.UP && isDirectionChangePossible){
                    isDirectionChangePossible = false;
                    direction = Direction.DOWN;
                }
                break;
            case KeyEvent.VK_SPACE: // restart game
                startGame();
                TIMER.start();
                break;
            case KeyEvent.VK_ENTER:
                startGame();
                TIMER.stop();
                RENDER_PANEL.revalidate();
                RENDER_PANEL.repaint();
            default:
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}


    public static Point getCookie() {
        return cookie;
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

    public static boolean isIsPossibleToMove() {
        return isPossibleToMove;
    }

    public static int getSnakeLength() {
        return snakeLength;
    }

    public static int getScore() {
        return score;
    }
}
