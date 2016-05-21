import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JPanel;

public class UpgradeScreen extends JPanel implements ActionListener {

	Main m;

	private JButton button1 = new JButton("Upgrade Sith Jump");
	private JButton button2 = new JButton("Upgrade Sith Damage");
	private JButton button3 = new JButton("Upgrade Sith Health");

	private JButton button4 = new JButton("Upgrade Jedi Jump");
	private JButton button5 = new JButton("Upgrade Jedi Damage");
	private JButton button6 = new JButton("Upgrade Jedi Health");

	private JButton button7 = new JButton("Play Game");
	private JButton button8 = new JButton("Get More Coins");

	private Jedi hero;
	private Sith villan;

	static int sithNumberOfCoins = 0;
	static int jediNumberOfCoins = 0;

	public UpgradeScreen(Main m) {
		super();
		this.m = m;
		setBackground(Color.WHITE);
		hero = new Jedi(550, 400, 100, 150, this);
		villan = new Sith(150, 410, 100, 140, this);
		button1.addActionListener(this);
		add(button1);
		button2.addActionListener(this);
		add(button2);
		button3.addActionListener(this);
		add(button3);
		button4.addActionListener(this);
		add(button4);
		button5.addActionListener(this);
		add(button5);
		button6.addActionListener(this);
		add(button6);
		button7.addActionListener(this);
		add(button7);
		button8.addActionListener(this);
		add(button8);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawString("Sith Number of Coins: "+sithNumberOfCoins, 150, 570);
		g.drawString("Jedi Number of Coins: "+jediNumberOfCoins, 550, 570);

		g.drawString("Jump: " + Jedi.jumpStrength, getXofCenteredString(g, "Jump :" + Jedi.jumpStrength)+400, getYofCenteredString(g, "Jump :" + Jedi.jumpStrength)-200);
		g.drawString("Damage: " + Jedi.damagePerStrike, getXofCenteredString(g, "Damage: " + Jedi.damagePerStrike)+400, getYofCenteredString(g, "Damage: " + Jedi.damagePerStrike)-100);
		g.drawString("Health: " + Jedi.health, getXofCenteredString(g, "Health: " + Jedi.health)+400, getYofCenteredString(g, "Health: " + Jedi.health));

		g.drawString("Jump: " + Sith.jumpStrength, getXofCenteredString(g, "Jump :" + Sith.jumpStrength), getYofCenteredString(g, "Jump :" + Sith.jumpStrength)-200);
		g.drawString("Damage: " + Sith.damagePerStrike, getXofCenteredString(g, "Damage: " + Sith.damagePerStrike), getYofCenteredString(g, "Damage: " + Sith.damagePerStrike)-100);
		g.drawString("Health: " + Sith.health, getXofCenteredString(g, "Health: " + Sith.health), getYofCenteredString(g, "Health: " + Sith.health));

		Graphics2D g2 = (Graphics2D)g;

		int width = getWidth();
		int height = getHeight();

		double ratioX = (double)width/800;
		double ratioY = (double)height/600;

		AffineTransform at = g2.getTransform();
		g2.scale(ratioX, ratioY);

		g2.drawLine(400, 0, 400, 600);

		hero.draw(g2, this);
		villan.draw(g2, this);
	}

	private int getXofCenteredString(Graphics g, String text) {
		Graphics2D g2d = (Graphics2D) g;
		FontMetrics fm = g2d.getFontMetrics();
		Rectangle2D r = fm.getStringBounds(text, g2d);
		int x = (400 - (int) r.getWidth()) / 2;
		return x;
	}
	private int getYofCenteredString(Graphics g, String text) {
		Graphics2D g2d = (Graphics2D) g;
		FontMetrics fm = g2d.getFontMetrics();
		Rectangle2D r = fm.getStringBounds(text, g2d);
		int y = (600 - (int) r.getHeight()) / 2 + fm.getAscent();
		return y;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(button1)) {
			if(sithNumberOfCoins >= 5) {
				Sith.jumpStrength += 10;
				sithNumberOfCoins -= 5;
			}
			repaint();
		}
		else if(e.getSource().equals(button2)) {
			if(sithNumberOfCoins >= 5) {
				Sith.damagePerStrike += 5;
				sithNumberOfCoins -= 5;
			}
			repaint();
		}		
		else if(e.getSource().equals(button3)) {
			if(sithNumberOfCoins >= 5) {
				Sith.health += 25;
				sithNumberOfCoins -= 5;
			}
			repaint();
		}
		else if(e.getSource().equals(button4)) {
			if(jediNumberOfCoins >= 5) {
				Jedi.jumpStrength += 5;
				jediNumberOfCoins -= 5;
			}
			repaint();
		}
		else if(e.getSource().equals(button5)) {
			if(jediNumberOfCoins >= 5) {
				Jedi.damagePerStrike += 5;
				jediNumberOfCoins -= 5;
			}
			repaint();
		}		
		else if(e.getSource().equals(button6)) {
			if(jediNumberOfCoins >= 5) {
				Jedi.health += 25;
				jediNumberOfCoins -= 5;
			}
			repaint();
		}
		else if(e.getSource().equals(button7)) {
			m.changePanel("4");
		}
		else if(e.getSource().equals(button8)) {
			ShipGameScreen.stop = false;
			ShipGameScreen.notOnPanel = false;
			m.changePanel("2");
		}
	}

}
