import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class GameClient {
    private Socket connectionSocket;
    private InetSocketAddress isa;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    public int playerIndex = -1;
    public Packet curPacket;

    public GameClient(String serverIP, int port) {
        isa = new InetSocketAddress(serverIP, port);
        try {
            connectionSocket = new Socket();
            connectionSocket.connect(isa, 1000);
            connectionSocket.setKeepAlive(true);
            System.out.println("[CLIENT] Client init success!");

            oos = new ObjectOutputStream(connectionSocket.getOutputStream());
            ois = new ObjectInputStream(connectionSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCmd(String cmd) {
        if (!cmd.equals("")) {
            try {
                oos.writeObject(cmd);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Packet recvPacket() {
        Object dataReceived;
        curPacket = null;
        if (ois != null) {
            try {
                dataReceived = ois.readObject();
                if (dataReceived instanceof Packet) {
                    curPacket = (Packet) dataReceived;
                    if (playerIndex == -1) 
                        playerIndex = curPacket.playerIndex;

                    for (int i = 0; i < 7; i++) {
                        curPacket.platforms[i].setY(curPacket.platformY[i]);
                        curPacket.platforms[i].setX(curPacket.platformX[i]);
                    }
                    
                    for (int i = 0; i < 2; i++) {
                        curPacket.players[i].setY(curPacket.playerY[i]);
                        curPacket.players[i].setX(curPacket.playerX[i]);
                    }
                }

            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("[CLIENT] ois error");
            return null;
        }
        
        return curPacket;
    }

    public void close() throws IOException {          
        oos.close();
        ois.close();
        connectionSocket.close();
        System.out.println("[CLIENT] Player" + (playerIndex+1) + " socket cloesd.");
    }
}