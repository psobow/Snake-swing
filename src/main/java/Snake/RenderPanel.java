package Snake;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class RenderPanel extends JPanel {
    private static RenderPanel renderPanelInstance = null;


    private final int FRAME_WIDTH = Snake.getFrameWidth();
    private final int FRAME_HEIGHT = Snake.getFrameHeight();
    private final int SCALE = Snake.getScale();

    private RenderPanel() {}

    public static RenderPanel getInstance(){
        if(renderPanelInstance == null){
            synchronized (RenderPanel.class){
                if(renderPanelInstance == null){
                    renderPanelInstance = new RenderPanel();
                }
            }
        }
        return renderPanelInstance;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // background
        g.setColor(Color.BLACK);
        g.fillRect(0,0, FRAME_WIDTH, FRAME_HEIGHT);

        // cookie
        g.setColor(Color.ORANGE);
        g.fillRect(Snake.getCookie().x * SCALE, Snake.getCookie().y * SCALE
                ,SCALE,SCALE);

        // snake
        if (Snake.isIsPossibleToMove()) g.setColor(Color.GREEN);
        else g.setColor(Color.RED);
        for (Point point : Snake.snakeParts){
            g.fillRect(point.x * SCALE, point.y * SCALE, SCALE, SCALE);
        }

        // statistics
        g.setColor(Color.WHITE);
        g.drawString("Score: " + Snake.getScore(),
                        Snake.getFrameWidth() / 2 + 20,
                        10);
    }
}
