import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class GameClient implements Runnable {
    private Socket connectionSocket;
    private InetSocketAddress isa;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Boolean isConnected = false;
    
    public int playerIndex = -1;
    public Packet packet;

    public GameClient(String serverIP, int port) {
        isa = new InetSocketAddress(serverIP, port);
        try {
            connectionSocket = new Socket();
            connectionSocket.connect(isa, 1000);
            connectionSocket.setKeepAlive(true);
            System.out.println("Client init success!");

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
        Packet curPacket = null;
        if (ois != null) {
            try {
                dataReceived = ois.readObject();
                if (dataReceived instanceof Packet) {
                    curPacket = (Packet) dataReceived;
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
            System.out.println("ois error");
            return null;
        }
        
        return curPacket;
    }

	@Override
	public void run() {
		while (true) {
            /* Keep fatching packet */
            this.packet = recvPacket();
            //System.out.println("client: " + curPacket.platforms[5].toString() + curPacket.platformY[5]);
            //System.out.println("run");
        }
	}


}