import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

public class Gift {
    int x, y, x_margin, y_margin;
    GiftType giftType;
    public Gift(int x, int y, int x_margin, int y_margin) {
        Random random = new Random();
        int option = random.nextInt(7);
        if (option == 0) {
            giftType = GiftType.bigger;
        }
        else if (option == 1) {
            giftType = GiftType.fast;
        }
        else if (option == 2) {
            giftType = GiftType.fireBall;
        }
        else if (option == 3) {
            giftType = GiftType.multiPly;
        }
        else if (option == 4){
            giftType = GiftType.slower;
        }
        else if (option == 5) {
            giftType = GiftType.confused;
        }
        else
            giftType = GiftType.random;
    }

    public Gift(int x, int y, int x_margin, int y_margin, boolean no_random) {
        Random random = new Random();
        int option = random.nextInt(6);
        if (option == 0) {
            giftType = GiftType.bigger;
        }
        else if (option == 1) {
            giftType = GiftType.fast;
        }
        else if (option == 2) {
            giftType = GiftType.fireBall;
        }
        else if (option == 3) {
            giftType = GiftType.multiPly;
        }
        else if (option == 4) {
            giftType = GiftType.confused;
        }
        else{
            giftType = GiftType.slower;
        }

    }
    public Gift(int x, int y, int x_margin, int y_margin, GiftType giftType) {
        Random random = new Random();
        int option = random.nextInt(5);
        this.giftType = giftType;
    }
    public void update() {
        y += 3;
    }

    public void save(PrintStream print) {
        print.println(x + " " + x_margin + " " + y + " " + y_margin + " " + giftType);
    }

    public Gift(Scanner scan) {
        x = scan.nextInt();
        x_margin = scan.nextInt();
        y = scan.nextInt();
        y_margin = scan.nextInt();
        giftType = GiftType.valueOf(scan.next());
    }
}
