import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Background {
	private ImageIcon background = new ImageIcon("img/bg.jpg");
	private int width, height;
	
	public Background() {
		this.width = background.getIconWidth();
		this.height = background.getIconHeight();
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(this.background.getImage(), 9, 0, null);
	}
	public Rectangle getRect() {
		return new Rectangle(0, 0, width, height);
	}
}
