import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class TopSpike {
	private ImageIcon topSpike = new ImageIcon("img/top_spike.png");
	private int width, height;
	
	public TopSpike() {
		this.width = topSpike.getIconWidth();
		this.height = topSpike.getIconHeight();
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(this.topSpike.getImage(), 9, 0, null);
	}
	
	public Rectangle getRect() {
		return new Rectangle(0, 0, width, height);
	}
	
}
