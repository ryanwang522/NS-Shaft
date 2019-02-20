import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;

import javax.swing.*;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	private ImageIcon imgPlayerEast, imgPlayerWest, imgInjuredEast, imgInjuredWest;
	private int xPos, yPos, height, width;
	protected int live = 12, previousPlatform = -1, curDirection, curPlatform;
	public static final int EAST = 1, WEST = 0;
	public boolean isInjured;
	
	public Player(Character playerNum) {
		this.curDirection = -1;
		this.curPlatform = -1;
		this.imgPlayerEast = new ImageIcon("img/caveman" + playerNum + EAST + ".png");
		this.imgPlayerWest = new ImageIcon("img/caveman" + playerNum + WEST + ".png");
		this.imgInjuredEast = new ImageIcon("img/caveman" + playerNum + EAST + "_injured.png");
		this.imgInjuredWest = new ImageIcon("img/caveman" + playerNum + WEST + "_injured.png");
		this.width = imgPlayerEast.getIconWidth();
		this.height = imgPlayerEast.getIconHeight();
		this.xPos = 310 - width/2;
		this.yPos = 205 - height/2;
		this.live = 12;
		this.isInjured = false;
		this.previousPlatform = -1;
	}
	
	public void draw(Graphics2D g) {
		if (isInjured) {
			if (curDirection == EAST)
				g.drawImage(imgInjuredEast.getImage(), xPos, yPos, null);
			else 
				g.drawImage(imgInjuredWest.getImage(), xPos, yPos, null);
		} else {
			if (curDirection == EAST)
				g.drawImage(imgPlayerEast.getImage(), xPos, yPos, null);
			else 
				g.drawImage(imgPlayerWest.getImage(), xPos, yPos, null);
		}
	}
	
	public int getHeight() { return height; }
	public int getWidth() { return width; }
	public int getX() { return xPos; }
	public int getY() { return yPos; }
	public int getDirection() { return curDirection; }
	
	public void moveDown() {
		yPos++;

	}
	
	public void moveUp() {
		yPos--;
	}
	
	public void setLocation(int x, int y) {
		xPos = x;
		yPos = y;
	}
	
	public void setDirection(int dir) {
		curDirection = dir;
	}
	public void setX(int x) {
		xPos = x;
	}
	public void setY(int y) {
		yPos = y;
	}
	
	public void changeLive(int changeNumber) {
		this.live += changeNumber;
		if (this.live > 12) this.live = 12;
		if (this.live < 0 ) this.live = 0;
	}
	
	public void changeLive(int changeNumber, int platformID) {
		if (platformID != this.previousPlatform ) this.live += changeNumber; 
		if (this.live > 12) this.live = 12;
		if (this.live < 0 ) this.live = 0;
	}
	
	// bottom rectangle checks collision with platforms
	public Rectangle getRectBottom() {
		Rectangle r = new Rectangle(xPos, yPos + height, width/2, 2);
		return r;
	}
	
	// top rectangle checks collision with spikes at the top
	public Rectangle getRectTop() {
		Rectangle r = new Rectangle(xPos, yPos, 10, 1);
		return r;
	}
	
	public void injured() {
		isInjured = true;
	}
	
	public void notInjured() {
		isInjured = false;
	}
	
}
