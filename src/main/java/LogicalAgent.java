

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalTime;
import java.util.*;
import java.util.Timer;

public class LogicalAgent {
    public LinkedList<Wall> walls;
    public LinkedList<Ball> balls;
    public LinkedList<Gift> gifts;
    public LinkedList<String> names;
    public LinkedList<Integer> scores;
    public Board board;
    public int life_left;
    public boolean blinking;
    public GiftType power;
    public LocalTime timerPowerUp, timerAddRow, timerBlink;
    public int score;
    public String lastFileName;
    GraphicAgent graphicAgent;
    Timer timer;
    public LogicalAgent() {
        board = new Board(290, 670, 50, 10);
        balls = new LinkedList<>();
        names = new LinkedList<>();
        scores = new LinkedList<>();
        File file = new File("./data/scoreBoard");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Scanner scan = new Scanner(fileInputStream);
            while (scan.hasNextLine()) {
                names.add(scan.nextLine());
                scores.add(Integer.parseInt(scan.nextLine()));
            }
            scan.close();
            fileInputStream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        walls = new LinkedList<>();

        gifts = new LinkedList<>();
        life_left = 3;
        blinking = false;
        timer = new Timer();
        power = null;
        timerPowerUp = null;
        timerAddRow = LocalTime.now();
        timerBlink = LocalTime.now();
        score = 0;
        lastFileName = "null";
    }
    public void setGraphicAgent(GraphicAgent g) {
        graphicAgent = g;
    }

    public void initialTimer () {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Toolkit.getDefaultToolkit().sync();
                graphicAgent.BuildState();
            }
        }, 1, 30);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateBalls();
                updateGifts();

            }
        }, 1, 20);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateWallBall();
                update();
            }
        }, 0, 20);
    }
    public void rightWall(Ball ball) {
        if ((ball.radius < Math.PI / 2.0 || ball.radius > 3.0 * Math.PI / 2.0)) {
            if (ball.radius < Math.PI / 2.0)
                ball.radius = Math.PI - ball.radius;
            else
                ball.radius = 2 * Math.PI - ball.radius +  Math.PI ;

        }
    }

    public void leftWall(Ball ball) {
        if (ball.radius > Math.PI / 2.0 && ball.radius < 3.0 * Math.PI / 2.0) {
            if (ball.radius < Math.PI)
                ball.radius = Math.PI - ball.radius;
            else
                ball.radius = 3.0 * Math.PI - ball.radius;

        }
    }

    public void downWall(Ball ball) {
        if (ball.radius >= Math.PI) {
            ball.radius = 2.0 *  Math.PI - ball.radius;

        }
    }

    public void upWall(Ball ball) {
        if (ball.radius <= Math.PI) {
            ball.radius = 2.0 * Math.PI - ball.radius;
        }
    }
    public void updateBalls() {
        LinkedList<Ball> balls = new LinkedList<>(this.balls);
        for (Ball ball : balls) {
            if (power == GiftType.fast)
                ball.update(4);
            else if (power == GiftType.slower)
                ball.update(-2);
            else
                ball.update();
        }
    }

    public void updateWallBall() {
        LinkedList<Ball> balls = new LinkedList<>(this.balls);
        int tmp2 = 0;
        if (power == GiftType.fast)
            tmp2 = 4;
        for (Ball ball: balls) {
            if (ball.y + 10 + ball.speed + tmp2 >= board.y - 10 && Math.abs(ball.y + 10 - (board.y - 10)) >= 10
                    && Math.abs(ball.x - board.x) <= board.x_margin
                    && ball.radius > Math.PI) {
                double tmp = ball.radius;
                ball.radius = 2 *  Math.PI - ball.radius;
                if (ball.radius <= 1.5 * Math.PI) {
                    double tmp3 = Math.PI - ball.radius;
                    ball.radius += ( tmp3 - ball.radius) * (Math.abs(ball.x - board.x)/ board.x_margin);
                }
                else {
                    double tmp3 = Math.PI - ball.radius;
                    ball.radius += ( tmp3 - ball.radius) * (Math.abs(ball.x - board.x)/ board.x_margin);
                }
                if (1.0 * Math.abs(ball.x - board.x) / board.x_margin >= 0.7) {
                    if (ball.x < board.x)
                        ball.radius = 0.75 * Math.PI;
                    else
                        ball.radius = 0.25 * Math.PI;
                }
            }
            else if (ball.y <= 10 && ball.radius < Math.PI) {
                upWall(ball);
            }
            else if (ball.x < 11) {
                leftWall(ball);
            }
            else if (ball.x > 564) {
                rightWall(ball);
            }
            LinkedList<Wall> walls = new LinkedList<>(this.walls);
            for (Wall wall: walls) {
                if (wall.wallType == WallType.blinking && blinking)
                    continue;

                if (wall.leftSide() <= ball.x && ball.x <= wall.rightSide()) {
                    if (Math.abs(wall.upSide() - (ball.y + 10)) <= ball.speed + tmp2) {
                        System.out.println("up wall");
                        if (power == GiftType.fireBall) {
                            wall.hp = 0;
                        }
                        else {
                            downWall(ball);
                            wall.hp--;
                        }
                    }
                    else if (Math.abs(wall.downSide() - (ball.y - 10)) <= ball.speed + tmp2) {
                        if (power == GiftType.fireBall) {
                            wall.hp = 0;
                        }
                        else {
                            upWall(ball);
                            wall.hp--;
                        }
                    }
                }
                else if (wall.upSide() <= ball.y && wall.downSide() >= ball.y) {
                    if (Math.abs(wall.rightSide() - (ball.x - 10)) <= ball.speed + tmp2) {
                        if (power == GiftType.fireBall) {
                            wall.hp = 0;
                        }
                        else {
                            leftWall(ball);
                            wall.hp--;
                        }
                    }
                    else if (Math.abs(wall.leftSide() - (ball.x + 10)) <= ball.speed + tmp2) {
                        if (power == GiftType.fireBall) {
                            wall.hp = 0;

                        }
                        else {
                            rightWall(ball);
                            wall.hp--;
                        }

                    }
                }
                if (wall.hp == 0) {
                    this.walls.remove(wall);
                    if (wall.wallType == WallType.wood)
                        score+=20;
                    else
                        score+=10;
                    if (wall.wallType == WallType.gift) {
                        wall.gift.x = wall.x;
                        wall.gift.y = wall.y;
                        gifts.add(wall.gift);
                    }
                }
            }
            if (ball.y + 10 >= board.y - 10 && Math.abs(ball.y + 10 - (board.y - 10)) >= 20) {
                this.balls.remove(ball);
            }
        }
        if (this.balls.isEmpty()) {
            life_left--;
            graphicAgent.hearts.getLast().setVisible(false);
            graphicAgent.hearts.removeLast();
            checkforover();
            graphicAgent.BuildState();
            graphicAgent.play.setVisible(true);
            timer.cancel();
        }
    }
    void checkforover() {
        if (isGameOver()) {
            graphicAgent.frame.setVisible(false);
            String name = JOptionPane.showInputDialog("Enter NickName:");
            File file = new File("./data/scoreBoard");
            try {
                names.add(name);
                scores.add(score);
                for (int i = 1; i < names.size(); i++) {
                    for (int j = i - 1; j >= 0; j--) {
                        if (scores.get(j) < scores.get(j + 1)) {
                            int tmp6 = scores.get(j);
                            scores.set(j, scores.get(j + 1));
                            scores.set(j + 1, tmp6);
                            String tmp5 = names.get(j);
                            names.set(j, names.get(j + 1));
                            names.set(j + 1, tmp5);
                        } else
                            break;
                    }
                }
                file.delete();
                file.createNewFile();
                PrintStream print = new PrintStream(new FileOutputStream(file));
                for (int i = 0; i < names.size(); i++) {
                    print.println(names.get(i));
                    print.println(scores.get(i));
                }
                print.close();
                System.exit(0);
            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        }
    }
    public void addWalls() {
         for (Wall wall : walls) {
             wall.y += 20;
             if (wall.y >= board.y - board.y_margin) {
                 life_left = 0;
                 for (JLabel label : graphicAgent.hearts)
                     label.setVisible(false);
                 graphicAgent.hearts.clear();
                 checkforover();
             }

         }
         addRow();
    }

    public void addRow() {
        int margin = 55;
        int width = 50;
        Random rand = new Random();
        for (int i = margin + width / 2; i + width + margin < 600; i += width) {
            int option = rand.nextInt(5);
            if (option == 0) {
                walls.add(new Wall(i, 10, width / 2, 10, WallType.wood));
            }
            else if (option == 1) {
                walls.add(new Wall(i, 10, width / 2, 10, WallType.glass));
            }
            else if (option == 2) {
                walls.add(new Wall(i, 10, width / 2, 10, WallType.blinking));
            }
            else if (option == 3) {
                walls.add(new Wall(i, 10, width / 2, 10, WallType.hidden));
            }
            else {
                walls.add(new Wall(i, 10, width / 2, 10, WallType.gift, new Gift(i, 10, width / 2, 10)));
            }

        }
    }

    public void updateGifts() {
        LinkedList<Gift> gifts = new LinkedList<>(this.gifts);
        for (Gift gift: gifts) {
            gift.update();
            if (gift.y + 10 >= board.y - 10
                                && Math.abs(gift.x - board.x) <= board.x_margin + gift.x_margin) {
                    if (gift.giftType != GiftType.multiPly) {
                        unPower();
                        power = gift.giftType;
                        if (power == GiftType.bigger)
                            board.x_margin = 100;
                        if (power == GiftType.random) {
                            power = new Gift(0, 0, 0, 0, true).giftType;
                            if (power == GiftType.multiPly) {
                                power = null;
                                for (int i = 0; i < 2; i++)
                                    balls.add(new Ball(balls.getLast().x, balls.getLast().y, new Random().nextDouble() * 2 * Math.PI));
                                this.gifts.remove(gift);
                            }
                        }
                        else {
                            timerPowerUp = LocalTime.now().plusSeconds(30);
                            this.gifts.remove(gift);
                        }

                    }
                    else {
                        for (int i = 0; i < 2; i++)
                             balls.add(new Ball(balls.getLast().x, balls.getLast().y, new Random().nextDouble() * 2 * Math.PI));
                        this.gifts.remove(gift);
                    }
            }
            if (board.y + 20 <= gift.y)
                this.gifts.remove(gift);
        }
    }

    public void update() {
        if (timerPowerUp != null &&timerPowerUp.isBefore(LocalTime.now())) {
            System.out.println("timerup");
            unPower();
        }
        if (timerAddRow.isBefore(LocalTime.now())) {
            addWalls();
            timerAddRow = LocalTime.now().plusSeconds(10);
        }
        if (timerBlink.isBefore(LocalTime.now())) {
            blinking = !blinking;
            timerBlink = LocalTime.now().plusSeconds(1);
        }
    }
    public void speedUp() {
        for (Ball ball: balls)
            ball.speed++;
    }
    public boolean isGameOver() {
        return life_left == 0;
    }
    public void unPower() {
        if (power == GiftType.bigger)
            board.x_margin = 50;
        power = null;
        timerPowerUp = null;
    }
    public void start() {
        initialTimer();
    }

    public void save(File file) {
        try {
            PrintStream print = new PrintStream(new FileOutputStream(file));
            print.println(lastFileName);
            print.println(timerBlink.toNanoOfDay() - LocalTime.now().toNanoOfDay());
            if (power != null) {
                print.println(true);
                print.println(power);
                print.println(timerPowerUp.toNanoOfDay() - LocalTime.now().toNanoOfDay());
            }
            else
                print.println(false);
            print.println(timerAddRow.toNanoOfDay() - LocalTime.now().toNanoOfDay());
            print.println(blinking);
            print.println(score);
            print.println(life_left);
            print.println(walls.size());
            for (Wall wall : walls)
                wall.save(print);
            print.println(balls.size());
            for (Ball ball : balls)
                ball.save(print);
            print.println(gifts.size());
            for (Gift gift : gifts)
                gift.save(print);
            print.close();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    public void load(File file) {
        try {
            Scanner scan = new Scanner(new FileInputStream(file));
            lastFileName = scan.next();
            timerBlink = LocalTime.now().plusNanos(scan.nextLong());
            if (scan.nextBoolean()) {
                power = GiftType.valueOf(scan.next());
                timerPowerUp = LocalTime.now().plusNanos(scan.nextLong());
            }
            timerAddRow = LocalTime.now().plusNanos(scan.nextLong());
            blinking = scan.nextBoolean();
            score = scan.nextInt();
            life_left = scan.nextInt();
            int wallSize = scan.nextInt();
            walls = new LinkedList<>();
            for (int i = 0; i < wallSize ; i++)
                walls.add(new Wall(scan));
            int ballSize = scan.nextInt();
            balls = new LinkedList<>();
            for (int i = 0; i < ballSize ; i++)
                balls.add(new Ball(scan));
            int giftSize = scan.nextInt();
            gifts = new LinkedList<>();
            for (int i = 0; i < giftSize ; i++)
                gifts.add(new Gift(scan));
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
    public void restart() {
        timer.cancel();
        timer = new Timer();
        board = new Board(290, 670, 50, 10);
        balls = new LinkedList<>();
        names = new LinkedList<>();
        scores = new LinkedList<>();
        File file = new File("./data/scoreBoard");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Scanner scan = new Scanner(fileInputStream);
            while (scan.hasNextLine()) {
                names.add(scan.nextLine());
                scores.add(Integer.parseInt(scan.nextLine()));
            }
            scan.close();
            fileInputStream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        walls = new LinkedList<>();

        gifts = new LinkedList<>();
        life_left = 3;
        blinking = false;
        timer = new Timer();
        power = null;
        timerPowerUp = null;
        timerAddRow = LocalTime.now();
        timerBlink = LocalTime.now();
        score = 0;
        lastFileName = "null";
        graphicAgent.play.setVisible(true);
    }
}
