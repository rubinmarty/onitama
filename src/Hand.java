import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Hand extends JPanel {
	private GameData data;
	private int ID;
	
	{
		this.setLayout(new GridLayout(1,0,10,10));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}
	public Hand(int HandID, GameData data) {
		this.data = data;
		this.ID = HandID;
		
		switch (HandID) {
		case 1:
			this.add(new MoveCard(0,1,data));
			this.add(new MoveCard(1,1,data));
			break;
		case 2:
			this.add(new MoveCard(2,2,data));
			this.add(new MoveCard(3,2,data));
			break;
		case 3:
			this.add(new MoveCard(4,3,data));
			break;
		default:
			throw new RuntimeException();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (this.data.getActivePlayer().playerNumber == this.ID) {
			this.setBackground(Color.decode("#e4b84a").darker());
		} else {
			this.setBackground(Color.decode("#e4b84a"));
		}
		super.paintComponent(g);
	}
}
	