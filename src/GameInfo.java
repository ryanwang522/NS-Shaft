public class GameInfo {
    public Player players[];
    public Platform[] platforms = new Platform[7];
    public boolean isPlaying;
    public int winner = -1;

    public GameInfo(Player[] p, Platform[] plat, boolean start, int winner) {
        players = new Player[p.length];
        for (int i = 0; i < players.length; i++) 
            this.players[i] = p[i];
        for (int i = 0; i < platforms.length; i++){
            this.platforms[i] = plat[i];
            //System.out.println(platforms[i].toString());
        }
        this.isPlaying = start;
        this.winner = winner;
    }

    public void update(int winner) {
        //this.isPlaying = start;
        this.winner = winner;
    }

    public void updateEnv(Platform[] plat) {
        //for (int i = 0; i < players.length; i++) 
        //    this.players[i] = p[i];

        for (int i = 0; i < platforms.length; i++)
            this.platforms[i] = plat[i];

    }

    public void updataPlayer(int pIndex, int curDir) {
        this.players[pIndex].curDirection = curDir;
    }
}