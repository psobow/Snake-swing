package Snake;


import java.awt.*;

public class SnakeRunner {
    public static void main(String[] args){

        EventQueue.invokeLater(() ->
        {
            Snake snake = Snake.getInstance();
        });
    }
}
