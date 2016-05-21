import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import sun.audio.*;

public class MenuScreen extends JPanel implements ActionListener {

	Main w;

	public static final int DRAWING_WIDTH = 800;
	public static final int DRAWING_HEIGHT = 600;

	private MovingImage logo;

	private final MovingImage background;

	private JButton button1 = new JButton("Think of name for flappy bird shit");
	private JButton button2 = new JButton("Think of name for fighting shit");

	public MenuScreen(Main w) {
		this.w = w;
		logo = new MovingImage("Logo.png", DRAWING_WIDTH/2-200, 0, 400, 300);
		background = new MovingImage("Background.jpg", 0, 0, DRAWING_WIDTH, DRAWING_HEIGHT);
		button1.addActionListener(this);
		add(button1);
		button2.addActionListener(this);
		add(button2);
	}

	public void paintComponent(Graphics g) {
		background.draw(g, this);
		logo.draw(g, this);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(button1)) {
			w.changePanel("2");
			ShipGameScreen.stop = false;
		}
		else if(e.getSource().equals(button2)) {
			w.changePanel("4");
		}
	}
}