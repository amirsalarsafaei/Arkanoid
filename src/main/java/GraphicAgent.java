import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Random;

public class GraphicAgent implements KeyListener {
    JFrame frame, framePauseMenu;
    ImagePanel panel, panelMenu, panelPauseMenu;
    JLabel scoreLabel, powerLabel;
    LinkedList<JLabel> hearts;
    LogicalAgent logicalAgent;
    JButton play;
    public void initial() {
        logicalAgent.graphicAgent = this;
        frame = new JFrame("Naruto ShippoBall");
        framePauseMenu = new JFrame("PauseMenu");
        framePauseMenu.setSize(500, 500);
        JButton playButton = new JButton();
        JButton saveButton = new JButton();
        JButton restartButton = new JButton();
        playButton.setIcon(new ImageIcon("./resources/play.png"));
        saveButton.setIcon(new ImageIcon("./resources/save.png"));
        restartButton.setIcon(new ImageIcon("./resources/restart.png"));
        saveButton.setFocusable(false);
        restartButton.setFocusable(false);
        framePauseMenu.setLayout(null);
        panelPauseMenu = new ImagePanel(new ImageIcon("./resources/pause-menu-background.png").getImage());
        panelPauseMenu.setBounds(0, 0, 500, 500);
        playButton.setBounds(100, 200, 100, 100);
        saveButton.setBounds(200, 200, 100, 100);
        restartButton.setBounds(300, 200, 100, 100);
        panelPauseMenu.add(playButton);
        framePauseMenu.add(panelPauseMenu);
        framePauseMenu.add(saveButton);
        framePauseMenu.add(restartButton);
        framePauseMenu.setLayout(null);
        framePauseMenu.setResizable(false);
        framePauseMenu.setVisible(false);
        framePauseMenu.setLocationRelativeTo(null);

        hearts = new LinkedList<>();
        frame.setSize(900, 750);
        panel = new ImagePanel(
                new ImageIcon("./resources/background.jpg").getImage());
        panelMenu = new ImagePanel(new ImageIcon("./resources/scorePlot.png").getImage());
        panel.setBounds(0, 0, 563, 790);
        panelMenu.setBounds(563, 0, 375, 800);

        panelMenu.setLayout(null);
        for (int i = 0; i < Math.min(5, logicalAgent.names.size()); i++) {
            JLabel label = new JLabel();
            label.setBounds(3, 280 + i * 30, 100, 30);
            label.setText( i + 1 + " : " + logicalAgent.names.get(i) + " " + logicalAgent.scores.get(i));
            panelMenu.add(label);
        }
        for (int i = 0; i < 3; i++) {
            JLabel label = new JLabel();
            label.setBounds(10 + 30 * i, 680, 20, 20);
            label.setIcon(new ImageIcon("./resources/heart.png"));
            panelMenu.add(label);
            hearts.add(label);
        }
        scoreLabel = new JLabel("Score");
        powerLabel = new JLabel();
        scoreLabel.setBounds(3, 40,  100, 20);
        powerLabel.setBounds(14,  116, 100, 100);
        powerLabel.setBackground(Color.BLACK);
        scoreLabel.setLayout(null);
        powerLabel.setLayout(null);
        panelMenu.add(scoreLabel);
        panelMenu.add(powerLabel);
        JButton pauseButton = new JButton();
        pauseButton.setIcon(new ImageIcon("./resources/pause.png"));
        pauseButton.setBounds(120, 600, 100, 100);
        playButton.setFocusable(false);
        pauseButton.setFocusable(false);
        panelMenu.add(pauseButton);
        play = new JButton();
        play.setIcon(new ImageIcon("./resources/play.png"));
        play.setBounds(230, 300, 100, 100);
        panel.add(play);
        play.setFocusable(false);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logicalAgent.timer.cancel();
                File file = new File("./data/cache/paused");
                if (file.exists())
                    file.delete();
                try {
                    file.createNewFile();
                    logicalAgent.save(file);
                    frame.setVisible(false);
                    framePauseMenu.setVisible(true);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = new File("./data/cache/paused");
                logicalAgent.load(file);
                logicalAgent.initialTimer();
                frame.setVisible(true);
                framePauseMenu.setVisible(false);
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = null;
                if (!logicalAgent.lastFileName.equals("null")) {
                    int a = JOptionPane.showConfirmDialog(frame, "Do you want to save as last name?");

                    if (a == JOptionPane.YES_OPTION) {
                        file = new File("./data/saves/" + logicalAgent.lastFileName);
                        if (file.exists())
                            file.delete();
                        try {
                            file.createNewFile();
                            logicalAgent.save(file);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        return;
                    }
                    else if (a == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                }
                String name = JOptionPane.showInputDialog(frame, "Enter Save File Name:");
                file = new File("./data/saves/" + name);
                logicalAgent.lastFileName = name;
                if (file.exists())
                    file.delete();
                try {
                    file.createNewFile();
                    logicalAgent.save(file);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logicalAgent.restart();
                framePauseMenu.setVisible(false);
                panel.removeAll();
                panel.add(play);
                panel.revalidate();
                panel.repaint();
                frame.setVisible(true);
            }
        });
        panel.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.add(panelMenu);
        frame.add(panel);
        frame.getContentPane().setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.addKeyListener(this);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logicalAgent.balls.add(new Ball(290, 655, new Random().nextDouble() * Math.PI));
                logicalAgent.start();
                play.setVisible(false);
            }
        });
    }


    public void BuildState() {
        panel.removeAll();
        panel.add(play);
        scoreLabel.setText(String.valueOf(logicalAgent.score));
        powerLabel.setIcon(null);
        if (logicalAgent.power != null)
            powerLabel.setIcon(new ImageIcon("./resources/power-" + logicalAgent.power + ".png"));

        panel.add(graphicBoard(logicalAgent.board));
        for (Ball ball: logicalAgent.balls)
            panel.add(graphicBall(ball));
        for (Wall wall: logicalAgent.walls) {
            JLabel tmp = graphicWall(wall);
            if (tmp != null)
                panel.add(tmp);
        }
        for (Gift gift:logicalAgent.gifts)
            panel.add(graphicGift(gift));
        panel.revalidate();
        panel.repaint();
    }


    public JLabel graphicBoard(Board board) {
        JLabel label = new JLabel("Board");
        label.setIcon(new ImageIcon("./resources/Board.png"));
        label.setBounds(board.x - board.x_margin, board.y - board.y_margin,
                board.x_margin * 2, board.y_margin * 2);
        return label;
    }

    public JLabel graphicBall(Ball ball) {
        JLabel label = new JLabel("Ball");
        if (logicalAgent.power == GiftType.fireBall)
            label.setIcon(new ImageIcon("./resources/fireball.png"));
        else
            label.setIcon(new ImageIcon("./resources/ball.png"));
        label.setBounds((int)ball.x - 10, (int)ball.y - 10, 20, 20);
        return label;
    }

    public JLabel graphicWall(Wall wall) {
        JLabel label = new JLabel("Wall");
        if (wall.wallType == WallType.blinking && logicalAgent.blinking) {
            return null;
        }
        if (wall.wallType == WallType.hidden)
            return null;
        label.setIcon(new ImageIcon("./resources/wall-" + wall.wallType + ".png"));
        label.setBounds((int)wall.x - 25, (int)wall.y - 10, 50, 20);
        return label;
    }

    public JLabel graphicGift(Gift gift) {
        JLabel label = new JLabel("Gift");
       // System.out.println("printing gift graphic");
        label.setIcon(new ImageIcon("./resources/gift-" + gift.giftType + ".png"));
        //System.out.println("./resources/gift-" + gift.giftType + ".png");
        label.setBounds((int)gift.x - 25, (int)gift.y - 10, 50, 20);
        return label;
    }

    public GraphicAgent() {
        JFrame initialFrame = new JFrame();
        initialFrame.setLayout(null);
        initialFrame.setLocationRelativeTo(null);
        initialFrame.setResizable(false);
        initialFrame.setSize(500, 500);
        logicalAgent = new LogicalAgent();
        logicalAgent.graphicAgent = this;
        JButton loadButton = new JButton();
        loadButton.setText("Load");
        JButton newGameButton = new JButton();
        newGameButton.setText("New Game");
        loadButton.setBounds(150, 350, 100, 30);
        newGameButton.setBounds(250, 350, 100, 30);
        JList<String> list = new JList<>((new File("./data/saves/").list()));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollableList = new JScrollPane(list);
        scrollableList.setBounds(13, 20, 460, 300);
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = list.getSelectedIndex();
                if (idx != -1) {
                    logicalAgent.load(new File("./data/saves/" + (new File("./data/saves/").list()[idx])));
                    initialFrame.setVisible(false);
                    loadButton.setEnabled(false);
                    initial();
                }
            }
        });
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initialFrame.setVisible(false);
                newGameButton.setEnabled(false);
                loadButton.setEnabled(false);
                initial();
            }
        });
        initialFrame.add(loadButton);
        initialFrame.add(newGameButton);
        initialFrame.getContentPane().setLayout(null);
        initialFrame.setVisible(true);
        initialFrame.add(scrollableList);

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (logicalAgent.power == GiftType.confused)
                logicalAgent.board.left();
            else
                logicalAgent.board.right();
            logicalAgent.board.speed++;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (logicalAgent.power == GiftType.confused)
                logicalAgent.board.right();
            else
                logicalAgent.board.left();
            logicalAgent.board.speed++;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        logicalAgent.board.speed = 15;
    }
}
