import java.io.PrintStream;
import java.util.Scanner;

public class Wall {
    int x, y;
    int x_margin, y_margin, hp;
    WallType wallType;
    Gift gift;
    public Wall(int x, int y, int x_margin, int y_margin, WallType wallType) {
        this.x = x;
        this.y = y;
        this.x_margin = x_margin;
        this.y_margin = y_margin;
        this.wallType = wallType;
        if (wallType == WallType.wood)
            hp = 2;
        if (wallType == WallType.glass || wallType == WallType.blinking || wallType == WallType.hidden)
            hp = 1;
    }

    public Wall(int x, int y, int x_margin, int y_margin, WallType wallType, Gift gift) {
        this.x = x;
        this.y = y;
        this.x_margin = x_margin;
        this.y_margin = y_margin;
        this.wallType = wallType;
        hp = 1;
        this.gift = gift;
    }

    public int rightSide() {
        return x + x_margin;
    }

    public int leftSide() {
        return x - x_margin;
    }

    public int downSide() {
        return y + y_margin;
    }

    public int upSide() {
        return y - y_margin;
    }

    public void save(PrintStream print) {
        print.println(x + " " + x_margin + " " + y + " " + y_margin + " " + hp + " " + wallType);
        if (wallType == WallType.gift)
            gift.save(print);
    }

    public Wall(Scanner scan) {
        x = scan.nextInt();
        x_margin = scan.nextInt();
        y = scan.nextInt();
        y_margin = scan.nextInt();
        hp = scan.nextInt();
        wallType = WallType.valueOf(scan.next());
        if (wallType == WallType.gift)
            gift = new Gift(scan);
    }
}
