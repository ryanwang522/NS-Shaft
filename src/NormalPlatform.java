import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Random;
import javax.swing.ImageIcon;

public class NormalPlatform extends Platform implements Serializable{
	private static final long serialVersionUID = 1L;
	Random rand = new Random();
	
	public NormalPlatform() {
		this.image = new ImageIcon("img/normal_platform.png");
		this.width = image.getIconWidth();
		this.height = image.getIconHeight();
		xPos = rand.nextInt(605 - width);;
		yPos = 550;
	}
	
	
	@Override
	public Rectangle getRect() {
		return new Rectangle(xPos, yPos, width, height);
	}
	
	@Override
	public void interactWithPlayer(Player p) {
		p.changeLive(1, this.id);
	}
	
	

}
