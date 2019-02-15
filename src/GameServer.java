import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class GameServer {
    private ServerSocket serverSockets[] = new ServerSocket[2];
    private Socket playerSockets[] = new Socket[2];
    private String ip;
    public GameInfo info;
    Player players[] = new Player[2];
    Platform platforms[] = new Platform[7];
    int playerIndex = 0;
    ArrayList<Session> playerSessions = new ArrayList<>();
    ArrayList<InetAddress> playerIPs = new ArrayList<>();
    boolean isGameStart = false;      

    public GameServer(int port, GameInfo p) {
        /* Initialize */
        System.out.println("Server started!");
        for (int i = 0; i < players.length; i++) 
            this.players[i] = p.players[i];
        for (int i = 0; i < platforms.length; i++)
            this.platforms[i] = p.platforms[i];
        for (int i = 0; i < serverSockets.length; i++)
            try {
                serverSockets[i] = new ServerSocket(port+i);
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        
        try {
            //URL url_name = new URL("http://ipv4bot.whatismyipaddress.com"); 
            URL url_name = new URL("http://checkip.amazonaws.com"); 
            
            BufferedReader sc = 
                new BufferedReader(new InputStreamReader(url_name.openStream())); 

            ip = sc.readLine().trim(); 
            System.out.println("Server IP: " + ip);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.info = p;
    }
    public void listen() {
        /* Start connecting */
        try {
            while(true) {
                System.out.println("Start connecting...");
                // the socket for sending data to player (owned by server)
                playerSockets[playerIndex] = serverSockets[playerIndex].accept();
                playerSockets[playerIndex].setSoTimeout(5);
                playerIPs.add(playerSockets[playerIndex].getInetAddress());

                System.out.println("Accept: " + playerSockets[playerIndex].getInetAddress());
                playerIndex++;

                // wait for the two players
                if (playerIndex == 1) {
                    System.out.println(playerIPs.get(0));
                    //System.out.println(playerIPs.get(1));
                    System.out.println("There are two players, Game start!!!");
                    break;
                }
            } 

            // let server engine start
            this.isGameStart = true;
            // let client threads works
            info.isPlaying = true;

            /* Session start. Open two threads to serve two players respectively */
            for (int i = 0; i < playerSockets.length; i++) {
                System.out.println(info.toString());
                playerSessions.add(new Session(playerSockets[i], this.players, this.platforms, info, i));
                Thread gameThread = new Thread(playerSessions.get(i));
                System.out.println("Thread-" + String.valueOf(i) + " start!");
                gameThread.start();
            }

        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public String getServerIP() {
        return this.ip;
    }
}

class Session implements Runnable {
    private Socket socket;
    private Player players[] = new Player[2];
    private Platform platforms[] = new Platform[7];
    private GameInfo p;
    private int playerIndex = -1;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public Session(Socket socket, Player players[], Platform platforms[], GameInfo p, int pIndex) {
        // socket for sending data to player
        this.socket = socket;
        for (int i = 0; i < players.length; i++) 
            this.players[i] = p.players[i];
        for (int i = 0; i < platforms.length; i++)
            this.platforms[i] = p.platforms[i];
        this.p = p;
        this.playerIndex = pIndex;
        System.out.println("session: " + this.players[0]);
    }

    public void run() {

        try {
            oos = new ObjectOutputStream(this.socket.getOutputStream());
            ois = new ObjectInputStream(this.socket.getInputStream());
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("isPlaying: " + p.isPlaying);
        while(p.isPlaying) {
             
            try {
                /* Send location of all objects to player */
                Packet dataToSend = new Packet(this.players, this.platforms, p.isPlaying, p.winner, this.playerIndex);
                oos.writeObject(dataToSend);
                oos.flush();

                /* Read the movement of player */
                if (ois != null) {
                    Object dataReceived = ois.readObject();
                    if (dataReceived instanceof String) {
                        String str = (String) dataReceived;

                        if (str.equals("RIGHT")) {
                            // moveRight
                            //p.updataPlayer(playerIndex, 1);
                            p.players[playerIndex].curDirection = 1;
                            System.out.println(String.valueOf(playerIndex) + " moves RIGHT");
                        }
                        
                        if (str.equals("LEFT")) {
                            // moveLeft
                            //p.updataPlayer(playerIndex, 0);
                            p.players[playerIndex].curDirection = 0;
                            //System.out.println(p.players[playerIndex].curDirection);
                            System.out.println(String.valueOf(playerIndex) + " moves LEFT");
                        }
                        
                        if (str.equals("RELEASE")) {
                            p.players[playerIndex].curDirection = -1;
                        }
                        
                    }

                }
            } catch (IOException e) {
                //System.out.println("this? " + e.getMessage());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } 

            for (int i = 0; i < platforms.length; i++)
                this.platforms[i] = p.platforms[i];
        }
    }
}
