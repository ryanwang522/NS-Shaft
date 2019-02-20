public class CreateServer implements Runnable {
    GameServer server;
    int port;
    GameInfo info;
    public CreateServer(int port, GameInfo info) {
        this.port = port;
        this.info = info;
    }

    public GameServer getServer() {
        return server;
    }

    @Override
    public void run() {
        this.server = new GameServer(port, info);
        this.server.listen();
        System.out.println("[SERVER] Server create successfully!");
    }

}