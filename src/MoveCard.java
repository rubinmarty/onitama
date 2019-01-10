import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class MoveCard extends JPanel {
	final private int ID;
	final private int direction;
	final private GameData data;
	
	private JLabel label;
	private JComponent tinyBoard;
	
	public int TINY_BOARD_SIZE = 100;
	public int TINY_TILE_SIZE = TINY_BOARD_SIZE / 5;
	
	private MoveSet getMoveSet() {
		return data.getMoveSet(ID);
	}
	
	public MoveCard(int ID, int direction, GameData data) {
		this.ID = ID;
		this.direction = direction;
		this.data = data;
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				MoveCard.this.data.selCard = MoveCard.this.getMoveSet();					
				MoveCard.this.repaint();
			}
		});
		
		this.label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setPreferredSize(new Dimension(0, 16));
		
		this.tinyBoard = new JComponent() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				
				String moveCardName = getMoveSet().toString();
				MoveSet myMoves = MoveSet.valueOf(moveCardName); 
				for (int i=-2; i<3; i++) {
					for (int j=-2; j<3; j++) {
						
						g2.setColor(Color.BLACK);
						g2.fillRect(TINY_TILE_SIZE*(i+2), TINY_TILE_SIZE*(j+2), TINY_TILE_SIZE, TINY_TILE_SIZE);
						
						Color c = myMoves.contains( new Move(i, j) ) ? Color.BLUE : Color.GRAY;
						if (i==0 && j==0) {c = Color.RED;}
						g2.setColor(c);
						g2.fillRect(TINY_TILE_SIZE*(i+2) +1, TINY_TILE_SIZE*(j+2) +1, TINY_TILE_SIZE -2, TINY_TILE_SIZE -2);
						
					}
				}
			}
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(TINY_BOARD_SIZE,TINY_BOARD_SIZE);
			}
		};
		
		tinyBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		this.setLayout(new BorderLayout());
		this.add(label, BorderLayout.NORTH);
		//System.out.println("label size: " + label.getPreferredSize());
		this.add(tinyBoard, BorderLayout.CENTER);
		this.add(Box.createHorizontalStrut(16), BorderLayout.EAST);
		this.add(Box.createHorizontalStrut(16), BorderLayout.WEST);
		this.add(Box.createVerticalStrut(16), BorderLayout.SOUTH);
	}
	
	@Override
	public Dimension getPreferredSize() {
		//System.out.println("Movecard pref size " + this.getSize());
		return super.getPreferredSize();
	}
	
	public Dimension getMinimumSize() {
		System.out.println("Movecard min size " + this.getSize());
		return getPreferredSize();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (this.getMoveSet() == this.data.selCard) {
			this.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
		} else {
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		}		
		
		String moveCardName = getMoveSet().toString();
		label.setText(moveCardName);
		
		double totalWidth = this.getWidth();
		double totalHeight = this.getHeight();
		Graphics2D g2 = (Graphics2D) g;
		
		if (direction == 1) {
		} else if (direction == 2) {
			g2.rotate(Math.PI);
			g2.translate(-totalWidth, -totalHeight);
		} else if (direction == 3) {
			g2.rotate(-Math.PI / 2);
			g2.translate(-totalWidth, 0);
		} else {
			throw new RuntimeException();
		}
		
		super.paintComponent(g2);
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, (int) totalWidth, (int) totalHeight);
	}
}