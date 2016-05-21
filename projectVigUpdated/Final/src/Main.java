import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import sun.audio.*;
import javax.swing.*;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;

public class Main extends JFrame {

	static JPanel cardPanel;
	
	public Main(String title) {
		super(title);

		setBounds(100, 100, 800, 600);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    cardPanel = new JPanel();
	    CardLayout cl = new CardLayout();
	    cardPanel.setLayout(cl);
	    
	    MenuScreen panel1 = new MenuScreen(this);
		ShipGameScreen panel2 = new ShipGameScreen(this);    
	    DeathScreen panel3 = new DeathScreen(this);
	    FightingGameScreen panel4 = new FightingGameScreen(this);
	    UpgradeScreen panel5 = new UpgradeScreen(this);
	    
	    addKeyListener(panel2);
	    addKeyListener(panel3);
	    addKeyListener(panel4.new KeyHandler());
	
	    cardPanel.add(panel1,"1");
	    cardPanel.add(panel2,"2");
	    cardPanel.add(panel3, "3");
	    cardPanel.add(panel4, "4");
	    cardPanel.add(panel5, "5");
	    
	    add(cardPanel);
	
	    setVisible(true);
	    panel2.run();
	}

	public static void main(String[] args)
	{
		Main w = new Main("Game");
		
	}
  
	public void changePanel(String num) {
		((CardLayout)cardPanel.getLayout()).show(cardPanel, num);
		requestFocus();
	}
}