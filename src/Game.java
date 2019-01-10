import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	private JFrame frame;
	private PlayerType pt1 = PlayerType.HUMAN;
	private PlayerType pt2 = PlayerType.HUMAN;
	
	public void run() {
		// Top-level frame in which game components live
		this.frame = new JFrame("Onitama");
		frame.setLocation(100, 10);
		frame.setSize(600, 700);
		frame.setResizable(false);
		frame.setLayout(new GridBagLayout());
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.getContentPane().setBackground(new Color(0xE4, 0xB8, 0x4A)); //brownish
		launchMenu();
	}
	
	private void launchMenu() {
		frame.getContentPane().removeAll();
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		final PlayerTypeSelectButton ptsb1 = new PlayerTypeSelectButton(Color.GREEN, 250, 30);
		frame.add(ptsb1, c);
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		final PlayerTypeSelectButton ptsb2 = new PlayerTypeSelectButton(Color.GREEN, 250, 30);
		frame.add(ptsb2, c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		@SuppressWarnings("serial")
		Button newGameButton = new Button("New Game", Color.GREEN, 500, 60) {
			public void myEvent() {
				Game.this.pt1 = ptsb1.pt;
				Game.this.pt2 = ptsb2.pt;
				GameEngine engine = new GameEngine(pt1, pt2);
				Game.this.startNewGame(engine);
			}
		};
		frame.add(newGameButton, c);		
		
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 2;
		frame.add(Box.createVerticalStrut(40), c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 2;
		@SuppressWarnings("serial")
		Button replayButton = new Button("Previous Game Replay", Color.GREEN, 500, 60) {
			public void myEvent() {
				try {
					File f = new File("myReplay.txt");
					GameEngine engine = new GameEngine(f);
					Game.this.startNewGame(engine);
				} catch (IOException e) {
					//e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error loading your file.");
				}
			}
		};
		frame.add(replayButton, c);
		
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 2;
		frame.add(Box.createVerticalStrut(40), c);
		
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 2;
		@SuppressWarnings("serial")
		Button instructionsButton = new Button("Instructions", Color.GREEN, 500, 60) {
			public void myEvent() {
				Game.this.viewInstructions();
			}
		};
		frame.add(instructionsButton, c);
		
		frame.validate();
		frame.repaint();
		
	}
	
	private void startNewGame(final GameEngine engine) {
		frame.getContentPane().removeAll();
		GameField field = engine.field;
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 1;
		c.gridy = 0;
		frame.add(field.hand2, c);
		
		c.gridx = 1;
		c.gridy = 1;
		frame.add(Box.createVerticalStrut(5), c);
		
		c.gridx = 1;
		c.gridy = 2;
		frame.add(field.board, c);
		
		c.gridx = 1;
		c.gridy = 3;
		frame.add(Box.createVerticalStrut(5), c);
		
		c.gridx = 1;
		c.gridy = 4;
		frame.add(field.hand1, c);
		
		c.gridx = 2;
		c.gridy = 2;
		frame.add(Box.createHorizontalStrut(5), c);
		
		c.gridx = 3;
		c.gridy = 2;
		frame.add(field.centerHand, c);
		
		c.gridx = 3;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		
		@SuppressWarnings("serial")
		final Button restartButton = new Button("RESET", Color.RED, 120, 30) {
			public void myEvent() {
				engine.terminate();
				Game.this.startNewGame(new GameEngine(pt1, pt2));
			}
		};
		
		@SuppressWarnings("serial")
		final QuitToMenuButton quitButton = new QuitToMenuButton() {
			public void myEvent() {
				engine.terminate();
				super.myEvent();
			}
		};
		
		@SuppressWarnings("serial")
		JPanel buttonPanel = new JPanel() {{
			this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			this.setOpaque(false);
			this.add(Box.createVerticalStrut(12));
			this.add(quitButton);
			this.add(Box.createVerticalStrut(8));
			this.add(restartButton);
		}};
		
		frame.add(buttonPanel, c);
		
		engine.start();
		frame.validate();
		frame.repaint();
	}
	
	private void viewInstructions() {
		frame.getContentPane().removeAll();
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 1;
		c.gridy = 1;
		JLabel jl = new JLabel("<html>"
				+ "Each player begins the game with five pieces and two move cards. A fifth move card starts in the center and is not usable by either player." + "<br><br>"
				+ "Players take turns moving their pieces. Each turn, move one of your pieces in accordance with a move from one of your cards." + "<br><br>"
				+ "Use the mouse to select a piece and a card, then click on a space to move your piece there." + "<br><br>"
				+ "After you use a move card, exchange it with the central card." + "<br><br>"
				+ "Move a piece onto an enemy piece to capture it." + "<br><br>"
				+ "One piece is your Master. The Master begins the game on your home-row's center. " + "<br><br>"
				+ "To win, either capture the enemy Master, or bring your Master to your enemy's home-row's center." + "<br><br>"
				+ "</html>");
		jl.setPreferredSize(new Dimension(520,580));
		jl.setFont(jl.getFont().deriveFont((float) 20));
		frame.add(jl, c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		frame.add(new QuitToMenuButton(), c);
		
		frame.validate();
		frame.repaint();
	}

	@SuppressWarnings("serial")
	private class QuitToMenuButton extends Button {
		public QuitToMenuButton() {
			super("QUIT", Color.RED, 100, 30);
		}
		
		@Override
		public void myEvent() {
			Game.this.launchMenu();
		}
	}
	
	@SuppressWarnings("serial")
	private class PlayerTypeSelectButton extends RotatingButton {
		public PlayerType pt = PlayerType.HUMAN;
		
		public PlayerTypeSelectButton(Color color, int w, int h) {
			super(new String[] {"HUMAN", "EASY COMPUTER", "MEDIUM COMPUTER"}, color, w, h);
		}
		
		@Override
		public void myEvent(String currText) {
			switch (currText) {
			case "HUMAN":
				this.pt = PlayerType.HUMAN;
				break;
			case "EASY COMPUTER":
				this.pt = PlayerType.EASYAI;
				break;
			case "MEDIUM COMPUTER":
				this.pt = PlayerType.MEDIUMAI;
				break;
			default:
				throw new RuntimeException();
			}
		}
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}
