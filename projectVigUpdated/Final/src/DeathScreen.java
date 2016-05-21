import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JPanel;

public class DeathScreen extends JPanel implements KeyListener, ActionListener
{

	Main m;

	public static final int DRAWING_WIDTH = 800;
	public static final int DRAWING_HEIGHT = 600;

	private MovingImage explosion;
	private final MovingImage background;

	public DeathScreen (Main m) {
		super();
		this.m = m;
		/*	explosion = new MovingImage("Explosion.gif", DRAWING_WIDTH/2-DRAWING_WIDTH/4, 
				DRAWING_HEIGHT/2-DRAWING_HEIGHT/4, DRAWING_WIDTH/2, DRAWING_HEIGHT/2);*/
		background = new MovingImage("Background.jpg", 0, 0, DRAWING_WIDTH, DRAWING_HEIGHT);
		JButton button = new JButton("Return to Menu");
		button.addActionListener(this);
		add(button);
	}

	private int getXofCenteredString(Graphics g, String text) {
		Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(text, g2d);
        int x = (this.getWidth() - (int) r.getWidth()) / 2;
        return x;
	}
	private int getYofCenteredString(Graphics g, String text) {
		Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(text, g2d);
        int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        return y;
	}

	public void paintComponent(Graphics g)
	{

		background.draw(g, this);		

		//explosion.draw(g, this);
		Font f = new Font("TimesRoman", Font.BOLD, 25);
		g.setFont(f);
		g.setColor(Color.YELLOW);
        g.drawString("Game Over", getXofCenteredString(g, "Game Over"), getYofCenteredString(g, "Game Over")-75);
		g.drawString("SCORE: " + ShipGameScreen.getScore(), getXofCenteredString(g, "SCORE: " + ShipGameScreen.getScore()), getYofCenteredString(g, "SCORE: " + ShipGameScreen.getScore())-25);
		g.drawString("Press Enter to Retry", getXofCenteredString(g, "Press Enter to Retry"), getYofCenteredString(g, "Press Enter to Retry")+25);
	}
	


	public void keyTyped(KeyEvent e) {		
	}


	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			ShipGameScreen.setScore(0);
			m.changePanel("2");
			ShipGameScreen.stop = false;
		}
	}


	public void keyReleased(KeyEvent e) {
	}

	public void actionPerformed(ActionEvent e) {
		ShipGameScreen.setScore(0);
		m.changePanel("1");
		
	}
}
