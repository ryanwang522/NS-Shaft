import java.io.Serializable;
import java.util.*;

class Packet implements Serializable {
    public Player players[] = new Player[2];
    public Platform platforms[] = new Platform[7];
    public boolean isPlaying;
    public int winner;
    public int playerIndex = -1;
    public int platformX[] = new int[7];
    public int platformY[] = new int[7];
    public char platType[] = new char[7];
    public int playerX[] = new int[2];
    public int playerY[] = new int[2];
    public int lives;
    public static final long serialVersionUID = 1L;
    Map<String, String> map = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("NormalPlatform", "0");
            put("RollingLeftPlatform", "1");
            put("RollingRightPlatform", "2");
            put("SpikePlatform", "3");
            put("SpringPlatform", "4");
            put("TempPlatform", "5");
        }
    };

    public Packet(Player pls[], Platform pl[], boolean isPlaying, int winner, int pIndex) {
        for (int i = 0; i < players.length; i++)
            this.players[i] = pls[i];

        for (int i = 0; i < platforms.length; i++) {
            this.platforms[i] = pl[i];
            this.platType[i] = map.get(platforms[i].toString().split("@")[0]).charAt(0);
        }

        for (int i = 0; i < 7; i++) {
            this.platformX[i] = this.platforms[i].getX();
            this.platformY[i] = pl[i].getY();
        }

        for (int i = 0; i < 2; i++) {
            this.playerX[i] = this.players[i].getX();
            this.playerY[i] = this.players[i].getY();
        }

        this.isPlaying = isPlaying;
        this.winner = winner;
        this.playerIndex = pIndex;
        this.lives = pls[pIndex].live;
    }
}