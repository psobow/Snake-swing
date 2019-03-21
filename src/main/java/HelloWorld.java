import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HelloWorld {
    public static void main(String[] args){
        System.out.println("Hello World!");

        Point point = new Point(0, 0);
        List<Point> list = new ArrayList<>();
        list.add(point);

        point.setLocation(point.x+1, point.y+1);
        System.out.println(list.get(0));
    }
}
