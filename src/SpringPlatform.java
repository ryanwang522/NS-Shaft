import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.Timer;

public class SpringPlatform extends Platform implements ActionListener, Serializable {
	private static final long serialVersionUID = 1L;
	private Random rand = new Random();
	private Timer timer = new Timer(40, this);
	private int velocity = -27, initialPos, t = 0, a = 1;
	private Player currPlayer;
	
	public SpringPlatform() {
		this.image = new ImageIcon("img/spring_platform.png");
		this.width = image.getIconWidth();
		this.height = image.getIconHeight();
		xPos = rand.nextInt(605 - width);
		yPos = 550;
	}

	
	public Rectangle getRect() {
		return new Rectangle(xPos, yPos, width, height);
	}
	
	@Override
	public void interactWithPlayer(Player p) {
		p.changeLive(1, this.id);
		//p.setY(p.getY() - 50);
		currPlayer = p;
		this.resetJump();
	}
	
	public void resetJump() {
		velocity = -27; 
		t = 0;
		initialPos = currPlayer.getY();
		timer.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			t++;
			/* -14 is a try-and-error result */
			if (velocity < -14) {
				currPlayer.setY(this.initialPos + velocity * t + (int)Math.round(0.5 * a * t * t));
				velocity = velocity + a * 1;
			} else{
				//System.out.println("stop");
				timer.stop();
			}
		}
	}
}
