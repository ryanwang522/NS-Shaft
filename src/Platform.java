import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.*;

public abstract class Platform {
	ImageIcon image;
	protected int id, xPos, yPos, width, height;
	protected int direction;
	
	public void draw(Graphics2D g2) {
		g2.drawImage(this.image.getImage(), xPos, yPos, null);
	}
	
	
	public int getHeight() { return height; }
	public int getWidth() { return width; }
	public int getX() { return xPos; }
	public int getY() { return yPos; }
	public int getDirection() { return direction; }
	
	public void move() {
		yPos--;
	}
	
	public void setLocation(int x, int y) {
		this.xPos = x;
		this.yPos = y;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public void setX(int x) {
		this.xPos = x;
	}
	
	public void setY(int y) {
		this.yPos = y;
	}
	
	public abstract Rectangle getRect();

	public abstract void interactWithPlayer(Player p);
}
