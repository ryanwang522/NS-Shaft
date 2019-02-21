import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.io.*;

/* The game engine */
public class NSShaft extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    private JLabel lbLives, lbLevel, lbChoosedDifficulty, lbRecord;
    private JButton btnHelp, btnPlay, btnExit, btnDual, btnClearRecord;
    private int level = 0, lives = 12, seconds = 0, bestLevel = 0, prevSec = 0;
    private JMenuBar menuBar;
    private JMenu menu;
    private JRadioButtonMenuItem rbEasy, rbMedium, rbHard;
    private JCheckBoxMenuItem cbSpring, cbTemp, cbRolling;
    private Player player;
    private Player[] players;
    private boolean start = false, moving = false, pause = false, isSet = false;
    private Timer gameTimer, platformTimer;
    private Platform[] platforms;
    private static final int platformTypes = 6;
    private Random rnd;
    private TopSpike topSpike;
    private Background background;
    private File highscores = new File("highscores.txt");
    private String name = "", line = "";

    private int playerNum = 1;
    private int winner = -1;
    private GameInfo gameInfo;
    private GameClient client;
    private Packet ply2Packet;
    private String[] imgName = {
        "normal_platform", "rolling_platform_left",
        "rolling_platform_right" , "spike_platform",
        "spring_platform", "temp_platform" };

    public static void main(String[] args) {
        new NSShaft();
    }

    public NSShaft() {
        // lives
        lbLives = new JLabel();
        lbLives.setPreferredSize(new Dimension(100, 85));
        lbLives.setHorizontalAlignment(SwingConstants.CENTER);
        lbLives.setIcon(new ImageIcon("img/lives" + lives + ".png"));

        // level
        lbLevel = new JLabel();
        lbLevel.setFont(new Font("Century Gothic", Font.BOLD, 36));
        Color cf1 = new Color(216, 224, 66);
        lbLevel.setForeground(cf1);
        lbLevel.setText("Level " + level);
        lbLevel.setPreferredSize(new Dimension(160, 30));
        lbLevel.setHorizontalAlignment(SwingConstants.CENTER);

        // title
        JLabel lbTitle = new JLabel();
        lbTitle = new JLabel();
        lbTitle.setPreferredSize(new Dimension(400, 85));
        lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lbTitle.setIcon(new ImageIcon("img/title.png"));

        // difficulty
        JLabel lbDifficulty = new JLabel();
        Font f3 = new Font("Calibri", Font.PLAIN, 24);
        lbDifficulty.setFont(f3);
        lbDifficulty.setForeground(new Color(122, 122, 122));
        String pathDifficulty="img/difficulty.png";
        Icon iconDifficulty=new ImageIcon(pathDifficulty); 
        lbDifficulty.setIcon(iconDifficulty);
        lbDifficulty.setPreferredSize(new Dimension(190, 25));
        lbDifficulty.setAlignmentX(Component.CENTER_ALIGNMENT);

        // choosed diffculty, default medium
        lbChoosedDifficulty = new JLabel();
        lbChoosedDifficulty.setFont(f3);
        lbChoosedDifficulty.setForeground(new Color(122, 122, 122));
        lbChoosedDifficulty.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbChoosedDifficulty.setPreferredSize(new Dimension(190, 25));
        String pathMediumDifficulty="img/medium.png";
        Icon iconMediumDifficulty=new ImageIcon(pathMediumDifficulty); 
        lbChoosedDifficulty.setIcon(iconMediumDifficulty);

        // highest record
        JLabel lbRecordTitle = new JLabel();
        lbRecordTitle.setFont(f3);
        lbRecordTitle.setForeground(new Color(122, 122, 122));
        lbRecordTitle.setPreferredSize(new Dimension(190, 25));
        String pathRecord = "img/record.png";
        Icon iconRecord = new ImageIcon(pathRecord); 
        lbRecordTitle.setIcon(iconRecord);
        lbRecordTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        lbRecord = new JLabel();
        lbRecord.setText("0");
        lbRecord.setFont(f3);
        Color c2 = new Color(217, 85, 28);
        lbRecord.setForeground(c2);
        lbRecord.setPreferredSize(new Dimension(190, 25));
        lbRecord.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // uses file to read the highest record that was saved before
        try {
            BufferedReader in = new BufferedReader(new FileReader(highscores));
            line = in.readLine();
            bestLevel = Integer.parseInt(line);
            line = in.readLine();
            name = line;
            lbRecord.setText(String.valueOf(bestLevel) + " by " + name);
            in.close();
        } catch (IOException e) {
            bestLevel = 0;
            name = "";
        }
        
        // JButtons
        String pathClear="img/clear.png";
        Icon iconClear=new ImageIcon(pathClear);  
        btnClearRecord = new JButton(iconClear);
        btnClearRecord.setBorder(null);
        btnClearRecord.setContentAreaFilled(false);
        btnClearRecord.setFocusable(false);
        btnClearRecord.addActionListener(this);

        String pathHelp="img/help.png";
        Icon iconHelp=new ImageIcon(pathHelp);  
        btnHelp = new JButton(iconHelp);
        btnHelp.setBorder(null);
        btnHelp.setContentAreaFilled(false);
        btnHelp.setFocusable(false);
        btnHelp.addActionListener(this);

        String pathPlay="img/play.png";
        btnPlay = new JButton(pathPlay);
        Icon iconPlay=new ImageIcon(pathPlay);  
        btnPlay = new JButton(iconPlay);
        btnPlay.setBorder(null);
        btnPlay.setContentAreaFilled(false);
        btnPlay.setFocusable(false);
        btnPlay.addActionListener(this);
        
        String pathDual="img/dual.png";
        btnDual = new JButton(pathDual);
        Icon iconDual=new ImageIcon(pathDual);  
        btnDual = new JButton(iconDual);
        btnDual.setBorder(null);
        btnDual.setContentAreaFilled(false);
        btnDual.setFocusable(false);
        btnDual.addActionListener(this);
        
        String pathExit="img/exit.png";
        btnExit = new JButton(pathExit);
        Icon iconExit=new ImageIcon(pathExit);  
        btnExit = new JButton(iconExit);
        btnExit.setBorder(null);
        btnExit.setContentAreaFilled(false);
        btnExit.setFocusable(false);
        btnExit.addActionListener(this);

        // set buttons alignment
        btnClearRecord.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHelp.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDual.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // JMenu
        menuBar = new JMenuBar();
        menu = new JMenu("Options");
        menuBar.add(menu);

        // Radio buttons
        ButtonGroup difficulty = new ButtonGroup();
        rbEasy = new JRadioButtonMenuItem("Easy");
        difficulty.add(rbEasy);
        rbEasy.addActionListener(this);
        menu.add(rbEasy);
        rbMedium = new JRadioButtonMenuItem("Medium");
        rbMedium.setSelected(true);
        rbMedium.addActionListener(this);
        difficulty.add(rbMedium);
        menu.add(rbMedium);
        rbHard = new JRadioButtonMenuItem("Hard");
        rbHard.addActionListener(this);
        difficulty.add(rbHard);
        menu.add(rbHard);

        // checkboxes
        cbSpring = new JCheckBoxMenuItem("Spring platform");
        cbSpring.setSelected(true);
        cbSpring.addActionListener(this);
        menu.add(cbSpring);
        cbTemp = new JCheckBoxMenuItem("Temp platform");
        cbTemp.setSelected(true);
        cbTemp.addActionListener(this);
        menu.add(cbTemp);
        cbRolling = new JCheckBoxMenuItem("Rolling platform");
        cbRolling.setSelected(true);
        cbRolling.addActionListener(this);
        menu.add(cbRolling);

        gameTimer = new Timer(3, this);
        rnd = new Random();
        topSpike = new TopSpike();
        background =new Background();

        // panels
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 40, 20));
        Color rgb = new Color(56, 56, 56);
        topPanel.setBackground(rgb);
        topPanel.add(lbLives);
        topPanel.add(lbLevel);
        topPanel.add(lbTitle);
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(rgb);
        rightPanel.add(lbDifficulty);
        rightPanel.add(lbChoosedDifficulty);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btnHelp);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btnPlay);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btnDual);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btnExit);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(lbRecordTitle);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(lbRecord);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btnClearRecord);

        //setBackground(Color.black);
        setFocusable(true);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        addKeyListener(this);
        JFrame frame = new JFrame();
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(rightPanel, BorderLayout.EAST);
        frame.add(this, BorderLayout.CENTER);
        frame.setTitle("Lolita");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(810, 710);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setJMenuBar(menuBar);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        background.draw(g2);
        if (start) {
            for (int i = 0; i < gameInfo.players.length; i++)
                gameInfo.players[i].draw(g2);

            for (int i = 0; i < gameInfo.platforms.length; i++) {
                if (gameInfo.platforms[i] != null)
                    gameInfo.platforms[i].draw(g2);
                else 
                    System.out.println("null");
            }
        }
        topSpike.draw(g2);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnHelp) {
            if (start) {
                // help is pressed in the middle of the game
                if (!pause) {
                    pause = true;
                    gameTimer.stop();
                    platformTimer.stop();
                    JOptionPane.showMessageDialog(
                                    null,
                                    "Use left and right arrow keys to control the player.\nTry to get as deep in the cave as possible.\nTo continue, press the help button.",
                                    "NS-Shaftt",
                                    JOptionPane.INFORMATION_MESSAGE);
                } else {
                    pause = false;
                    gameTimer.start();
                    platformTimer.start();
                }
            } else {
                // the game hasn't started and the pause button is pressed
                JOptionPane.showMessageDialog(
                                null,
                                "Use left and right arrow keys to control the player.\nTry to get as deep in the cave as possible.\nYou can change settings by pressing the options button on the top left corner.\n If you want to pause the game, press the help button or press P.",
                                "NS-Shaftt", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (e.getSource() == btnClearRecord) {
            int option = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to clear the record?",
                    "NS-Shaftt",
                    JOptionPane.YES_NO_OPTION);
            if (option == 0) {
                // clearing the record
                if (highscores.delete()) {
                    JOptionPane.showMessageDialog(null,
                            "File deleted!",
                            "NS-Shaftt",
                            JOptionPane.INFORMATION_MESSAGE);
                    highscores = new File("highscores.txt");
                    bestLevel = 0;
                    name = "";
                    lbRecord.setText("0");
                } else {
                    JOptionPane.showMessageDialog(null, "File not deleted!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == btnPlay) {
            this.gameStart(1);
        } else if (e.getSource() == btnExit) {
            int option = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to exit?", "NS-Shaft",
                    JOptionPane.YES_NO_OPTION);
            if (option == 0) {
                System.exit(0);
            }
        } else if (e.getSource() == btnDual) {
            /* Exit individual mode */
            if (start) endGame();
            
            Object[] possibleValues = { "Server", "Client" }; 
            Object selectedValue = JOptionPane.showInputDialog(null, "Server or Clent?", "Selection Mode", 
                JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
            if (selectedValue == null) {
                return;
            } else if (selectedValue.toString().equals("Server")) {
                /* --- Server --- */
                /* Create two players and platforms */
                this.playerNum = 2;
                resetEnv();
                generatePlatforms();
                this.gameInfo = new GameInfo(this.players, this.platforms,
                    this.start, this.winner);

                CreateServer cs = new CreateServer(8000, gameInfo);
                Thread t = new Thread(cs);
                t.start();

                sleep(5000);

                GameServer server = cs.getServer();
                System.out.println("[ENV] Waiting for connect...");

                client = new GameClient(server.getServerIP(), 8000);

                while (!server.isGameStart) {}
                sleep(300);
                dualModeStart();
            
            } else {
                // TODO: Let user input server ip
                /* --- Client Player 2 --- */
                this.playerNum = 2;
                resetEnv();
                client = new GameClient("114.24.136.85", 8001);
                gameTimer.start();
                repaint();
                System.out.println("[ENV] Client game start!");
            }

        
        } else if (rbEasy.isSelected()) {
            String pathEasyDifficulty="img/easy.png";
            Icon iconEasyDifficulty=new ImageIcon(pathEasyDifficulty); 
            lbChoosedDifficulty.setIcon(iconEasyDifficulty);
        } else if (rbMedium.isSelected()) {
            String pathMediumDifficulty="img/medium.png";
            Icon iconMediumDifficulty=new ImageIcon(pathMediumDifficulty); 
            lbChoosedDifficulty.setIcon(iconMediumDifficulty);
        } else if (rbHard.isSelected()) {
            String pathHardDifficulty="img/hard.png";
            Icon iconHardDifficulty=new ImageIcon(pathHardDifficulty); 
            lbChoosedDifficulty.setIcon(iconHardDifficulty);
        }
        
        if (e.getSource() == gameTimer) {
            if (playerNum == 2 && client.playerIndex == -1) {
                /* receive packet to update playerIndex first */
                client.recvPacket();
                if (client.playerIndex == 1) {
                    // player 2
                    Packet p = client.curPacket;
                    gameInfo = new GameInfo(p.players, p.platforms, p.isPlaying, p.winner);
                }
                return;
            }

            if (playerNum == 1 || client.playerIndex == 0) {
                seconds++;
                if (seconds % 100 == 0) {
                    // the difficulty can be changed by pressing different radio
                    // buttons
                    if (rbEasy.isSelected()) {
                        platformTimer = new Timer(15, this);
                    } else if (rbMedium.isSelected()) {
                        platformTimer = new Timer(10, this);
                    } else if (rbHard.isSelected()) {
                        platformTimer = new Timer(8, this);
                    }

                    platformTimer.restart();

                }
                if (seconds % 1000 == 0 && seconds != 0) {
                    // when an amount of time has passed, the level will increase
                    level++;
                    lbLevel.setText("Level " + level);
                }
                
                for (int i = 0; i < gameInfo.players.length; i++) {
                    Player player = gameInfo.players[i];
                    int x = player.getX();

                    if (player.curDirection == 1)
                        if (x < 605 - player.getWidth()) player.setX(++x);
                    
                    if (player.curDirection == 0) 
                        if (x > 5) player.setX(--x);

                    // if player does not intersect any platforms, he moves down
                    // else if player intersects any platforms, he moves up
                    checkCurrentPlatform(player);
                    if (player.curPlatform == -1)
                        player.moveDown();
                    else 
                        player.moveUp();
                    
                    if (client != null) 
                        lbLives.setIcon(new ImageIcon("img/lives" + gameInfo.players[client.playerIndex].live + ".png"));

                    this.players[i] = gameInfo.players[i];
                    
                    if (players[i].live == 0 || players[i].getY() >= 600) {
                        gameInfo.update(1 - i);
                        endGame();
                        break;
                    }
                }
            } else {
                // Dual mode player2 client
                ply2Packet = client.recvPacket();
                if (ply2Packet != null) {
                    if (client.playerIndex == -1) 
                        client.playerIndex = ply2Packet.playerIndex;

                    // your player
                    gameInfo.players[0] = ply2Packet.players[client.playerIndex];
                    gameInfo.players[0].live = ply2Packet.lives;
                    // others player
                    gameInfo.players[1] = ply2Packet.players[1 - client.playerIndex];

                    lbLives.setIcon(new ImageIcon("img/lives" + gameInfo.players[0].live + ".png"));

                    for (int i = 0; i < gameInfo.platforms.length; i++) {
                        gameInfo.platforms[i] = ply2Packet.platforms[i];
                        gameInfo.platforms[i].image = 
                            new ImageIcon("img/" + imgName[ply2Packet.platType[i] - '0'] + ".png");
                    }

                    gameInfo.platforms = ply2Packet.platforms;
                    gameInfo.update(ply2Packet.winner);
                    this.start = ply2Packet.isPlaying;

                    if (gameInfo.winner != -1)  
                        endGame();
                    
                    ply2Packet = null;
                } else {
                    System.out.println("[ENV] Packet is null.");
                }
            }
            
        }

        if (e.getSource() == platformTimer) {
            /* Single mode or dual mode's server side */
            if (playerNum == 1 || client.playerIndex == 0) {
                for (int i = 0; i < platforms.length; i++) {
                    platforms[i].move();
                    int platformObjectID = System.identityHashCode(platforms[i]);
                    for (int j = 0; j < this.players.length; j++)
                        checkCollision(players[j], platforms[i], platformObjectID);
                    // generate a new platform when it leaves the screen
                    if (platforms[i].getY() + platforms[i].getHeight() <= 0) {
                        platforms[i] = getRandomPlatform(platformTypes);
                        platforms[i].setID(System.identityHashCode(platforms[i]));
                    }
                }
                this.gameInfo.updateEnv(this.platforms);
            }
            
        }
        
        /* The game hasn't started */ 
        if (!this.start) 
            return;

        /* Show injured image */
        for (int i = 0; i < gameInfo.players.length; i++) {
            Player player = gameInfo.players[i];
            if (player.isInjured) {
                if (!isSet) {
                    isSet = true;
                    prevSec = seconds;
                } else {
                    if (seconds - prevSec >= 10) {
                        player.isInjured = false;
                        isSet = false;
                    }
                }
            }
        }
            
        repaint();
    }
    
    /* Check whether a player p stands on a platform */
    public void checkCurrentPlatform(Player p) {
        if (p == null) return;
        p.curPlatform = -1;
        for (int i = 0; i < platforms.length; i++) 
            if (platforms[i] != null && p.getRectBottom().intersects(platforms[i].getRect())) {
                p.curPlatform = i;
                return;
            }
    }

    public void dualModeStart() {
        this.start = true;
        gameTimer.restart();
        menu.setEnabled(false);
        repaint();
        System.out.println("[ENV] Dual mode start...");
    }

    public void gameStart(int playerNum) {
        this.playerNum = playerNum;
        resetEnv(); // new palyers
        this.start = true;
        generatePlatforms(); // new platforms
        gameTimer.restart();

        this.gameInfo = new GameInfo(this.players, this.platforms,
                this.start, this.winner);
        menu.setEnabled(false);
        repaint();
        System.out.println("In start...");
    }
    
    public void generatePlatforms() {
        // NOTICE: Reset the env first then call this function
        
        for (int i = 0 ; i < platforms.length; i++) {
            /* Generate the platform for player to stand on when beginning */
            if (i == platforms.length / 2) {
                platforms[3] = new NormalPlatform();
                platforms[3].setLocation(260, 225);
                continue;
            }

            platforms[i] = getRandomPlatform(6);
            platforms[i].setID(System.identityHashCode(platforms[i]));
            platforms[i].setY(45 + i * 70);
        }
        
    }
    
    public Platform getRandomPlatform(int numOfType) {
        Platform platform = null;
        if (numOfType > 6) numOfType = 6;
        
        int n = rnd.nextInt(numOfType);
        if (n == 0) {
            platform = new NormalPlatform();
        } else if (n == 1) {
            platform = new SpikePlatform();
        } else if (n == 2) {
            platform = new SpringPlatform();
        } else if (n == 3) {
            platform = new TempPlatform();
        } else if (n == 4) {
            platform = new RollingLeftPlatform();
        } else if (n == 5) {
            platform = new RollingRightPlatform();
        }
        else {
            platform = new NormalPlatform();
        }
        
        return platform;
    }
    
    public void resetEnv() {
        //this.moveRight = false;
        //this.moveLeft = false;
        this.start = false;
        this.isSet = false;
        this.platforms = new Platform[7]; 
        
        Character[] pn = {'1', '2'};
        this.players = new Player[this.playerNum];
        for (int i = 0; i < playerNum; i++) {
            this.players[i] = new Player(pn[i]);
            this.players[i].live = 12;
        }
        if (this.playerNum == 1) player = players[0];

        this.client = null;
        this.seconds = 0;
        this.prevSec = 0;
        this.level = 0;
        this.lbLives.setIcon(new ImageIcon("img/lives" + lives + ".png"));
        this.lbLevel.setText("Level " + level);
    }
    
    public int isOnPlatform() {
        for (int i = 0; i < platforms.length; i++) 
            if (player.getRectBottom().intersects(platforms[i].getRect()))
                return i;
        return -1;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (start) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT && !pause) {
                
                if (playerNum > 1 && !moving) {
                    // Dual mode
                    client.sendCmd("LEFT");
                    gameInfo.players[0].curDirection = 0;
                } else {
                    players[0].curDirection = 0;
                }
                
            }
            // User clicks the right arrow key
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT && !pause) {
                if (playerNum > 1 && !moving) {
                    // Dual mode
                    client.sendCmd("RIGHT");
                    gameInfo.players[0].curDirection = 1;
                } else 
                    players[0].curDirection = 1;

            } else if (e.getKeyCode() == KeyEvent.VK_P) {
                // if player presses "p" during game, the game pauses
                if (!pause) {
                    pause = true;
                    gameTimer.stop();
                    platformTimer.stop();
                } else {
                    pause = false;
                    gameTimer.start();
                    platformTimer.start();
                }
            }
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (start) {
            if (playerNum > 1) {
                // Dual mode
                moving = false;
                client.sendCmd("RELEASE");
                gameInfo.players[0].curDirection = -1;
            } else {
                // Single mode clear the movement
                players[0].curDirection = -1;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void checkCollision(Player player, Platform platform, int platformIndex) {
        
        /* Player interacts with platform */
        if (platform.getRect().intersects(player.getRectBottom()) ) {
            //System.out.println("intersect platform: " + platformIndex);
            platform.interactWithPlayer(player);
            player.previousPlatform = platformIndex;
            if (playerNum == 1)
                lbLives.setIcon(new ImageIcon("img/lives" + player.live + ".png"));
        }
        
        /* Player interacts with top-spike */
        if (player.getRectTop().intersects(topSpike.getRect())) {
            player.changeLive(-5);
            if (playerNum == 1)
                lbLives.setIcon(new ImageIcon("img/lives" + player.live + ".png"));
            player.setY(player.getY() + 40);
        }
        
    }

    private void clearEnv() {
        this.start = false;

        lbLives.setIcon(new ImageIcon("img/lives" + players[0].live + ".png"));

        // stop the timers
        if (playerNum == 1 || client.playerIndex == 0)
            this.platformTimer.stop();
        this.gameTimer.stop();

        // clean the platforms
        for (int j = 0; j < platforms.length; j++) 
            platforms[j] = null;
        this.platforms = null;
        this.player = null;
        this.players = null;

        sleep(500);
        gameInfo.isPlaying = false;

        // Clean up the sockets
        if (client != null) {
            try {
                client.close();
            } catch (IOException ec) {
                ec.printStackTrace();
            }
        }

        btnPlay.setEnabled(true);
        menu.setEnabled(true);
    }

    public void endGame() {
        clearEnv();

        if (playerNum == 2) {
            JOptionPane.showMessageDialog(null, "Player" + (gameInfo.winner + 1) + " win!", 
                "NS-Shaft", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (level == 1) {
            JOptionPane.showMessageDialog(null, "You have completed " + level
                    + " level.", "NS-Shaft", JOptionPane.INFORMATION_MESSAGE);
        } else if (level != 1) {
            JOptionPane.showMessageDialog(null, "You have completed " + level
                    + " levels.", "NS-Shaft", JOptionPane.INFORMATION_MESSAGE);
        }
        // if player breaks the record
        if (level > bestLevel) {
            if (level != 1) {
                JOptionPane.showMessageDialog(null,
                        "Congratulations! You have set a new record of "
                                + level + " levels!", "NS-Shaft",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Congratulations! You have set a new record of "
                                + level + " level!", "NS-Shaft",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            bestLevel = level;
            // writing the new record and player's name into the file
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(
                        highscores));
                name = JOptionPane.showInputDialog("Please enter your name: ");
                if (name == null) {
                    name = "Anonymous";
                }
                if (name.length() == 0) {
                    name = "Anonymous";
                }
                out.write(String.valueOf(bestLevel));
                out.newLine();
                out.write(name);
                out.close();
                JOptionPane.showMessageDialog(null,
                        "Data has been written to the file!", "Finished!",
                        JOptionPane.INFORMATION_MESSAGE);
                lbRecord.setText(String.valueOf(bestLevel) + " by " + name);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage() + "!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}