import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class Board {
    int x, y, x_margin, y_margin, speed;
    public Board(int x, int y, int x_margin, int y_margin) {
        this.x = x;
        this.y = y;
        this.x_margin = x_margin;
        this.y_margin = y_margin;
        speed = 15;
    }

    public void right() {
        this.x += speed;
        this.x = Math.min(this.x, 580 - x_margin - 15);

    }

    public void left() {
        this.x -= speed;
        this.x = Math.max(this.x, x_margin);
    }

    public void save(PrintStream print) {
        print.println(x + " " + x_margin + " " + y + " " + y_margin + " " + speed);
    }

    public Board(Scanner scan) {
        x = scan.nextInt();
        x_margin = scan.nextInt();
        y = scan.nextInt();
        y_margin = scan.nextInt();
        speed = scan.nextInt();
    }
}
