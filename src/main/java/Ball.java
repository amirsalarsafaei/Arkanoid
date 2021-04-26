import java.io.PrintStream;
import java.util.Scanner;

public class Ball {
    double x, y;
    double speed;
    double radius;

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
        this.radius = 3 * Math.PI / 4.0;
        this.speed = 10;
    }

    public Ball(int x, int y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed = 6;
    }

    public Ball(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed = 6;
    }

    public void update() {
        x += speed * Math.cos(radius);
        x = Math.min(x, 570);
        x = Math.max(x, 10);
        y -= speed * Math.sin(radius);
        y = Math.min(y, 800);
        y = Math.max(y, 10);
    }
    public void update(int xspeed) {
        speed += xspeed;
        x += speed * Math.cos(radius);
        x = Math.min(x, 570);
        x = Math.max(x, 10);
        y -= speed * Math.sin(radius);
        y = Math.min(y, 800);
        y = Math.max(y, 10);
        speed -= xspeed;
    }

    public void save(PrintStream print) {
        print.println(x + " " + y + " " + speed + " " + radius);
    }

    public Ball(Scanner scan) {
        x = scan.nextDouble();
        y = scan.nextDouble();
        speed = scan.nextDouble();
        radius = scan.nextDouble();
    }
}
