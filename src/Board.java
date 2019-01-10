import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class Board extends JPanel {
	final private static int ROWS = 5;
	final private static int COLUMNS = 5;
	final private static int TILE_SIZE = 60;
	final private static int BORDER_SIZE = 10;

	final private static int heightP = ROWS*TILE_SIZE + 2*BORDER_SIZE;;
	final private static int lengthP = COLUMNS*TILE_SIZE + 2*BORDER_SIZE;
	
	private GameData data;
	private PlaySubmitter publicPlaySubmitter;
	
	public Board(GameData data, PlaySubmitter ps) {
		this.data = data;
		this.publicPlaySubmitter = ps;
		this.setLayout(new GridLayout(ROWS, COLUMNS));
		
		this.setBorder(
			BorderFactory.createCompoundBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.DARK_GRAY),
							BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.GRAY)
						),
					BorderFactory.createCompoundBorder(
							BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.LIGHT_GRAY),
							BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.WHITE)
						)
				)
			);
		
		for (int y = 0; y<ROWS; y++) {
			for (int x = 0; x<COLUMNS; x++) {
				this.add(new Tile(x, y));
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	@Override
	public Dimension getPreferredSize() {
		//System.out.println("Board size: " + this.getSize());
		return new Dimension(lengthP, heightP);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(lengthP, heightP);
	}
	
	public class Tile extends JPanel  {
		private int i;
		private int j;
		private Vector v;
		private GameData data = Board.this.data;
		private boolean isTemple;
		
		//tile content info
		private final Color DEFAULT_COLOR = Color.GRAY;
		private final Color TEMPLE_COLOR = Color.GRAY.darker();
		private final Color PREVIEW_COLOR = Color.PINK;
		
		//tile border info
		private final int BORDER_THICKNESS = 2;
		
		//piece rendering info
		private final int PIECE_RADIUS = 15;
		private final int MASTER_BORDER = 2;
		private final int SELECTED_PIECE_BORDER_THICKNESS = 2;
		private final Color SELECTED_PIECE_BORDER_COLOR = Color.YELLOW;
		
		public Tile(int i, int j) {
			this.i = i;
			this.j = j;
			this.v = new Vector(i,j);
			this.isTemple = (v.equals(new Vector(2,0)) || v.equals(new Vector(2,4)));
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					int h = Tile.this.getHeight();
					int w = Tile.this.getWidth();
					
					if (w/2 - PIECE_RADIUS < x && x < w/2 + PIECE_RADIUS
							&& h/2 - PIECE_RADIUS < y && y < h/2 + PIECE_RADIUS) {
						Tile.this.onMyPieceClick();
					} else {
						Tile.this.onClick();
					}
					
					Tile.this.repaint();
				}
			});
		
		}
		
		private void onClick() {
			if (data.selCard == null || data.selPiece == null) {
				return;
			}
			Play play = data.getPlayIfValid(data.selCard, this.v, data.selPiece, data.getActivePlayer());
			if (play != null) {
				Board.this.publicPlaySubmitter.submitPlay(play);
			}
		}
		
		private void onMyPieceClick(){
			Piece newSelPiece = data.readOnlyBoardData.getPiece(this.i, this.j);
			if (newSelPiece != null) {
				data.selPiece = newSelPiece;
			} else {
				this.onClick();
			}
		}
		
		public void paintComponent(Graphics g) {
			int h = getHeight();
			int w = getWidth();
			Color color = this.isTemple ? TEMPLE_COLOR : DEFAULT_COLOR;
			
			if (data.getPlayIfValid(data.selCard, this.v, data.selPiece, data.getActivePlayer()) != null) {
				color = PREVIEW_COLOR;
			}
			
			this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, DEFAULT_COLOR.darker(), DEFAULT_COLOR.brighter()));
			
			super.paintComponent(g);
			
			g.setColor(color);
			g.fillRect(BORDER_THICKNESS, BORDER_THICKNESS, w-2*BORDER_THICKNESS, h-2*BORDER_THICKNESS);
			
			Piece myPiece = data.readOnlyBoardData.getPiece(i, j);
			Piece selPiece = data.selPiece;
			if (myPiece != null) {
				PieceType tp = myPiece.pieceType;
				int playerNumber = myPiece.owner.playerNumber;
				
				//color selection border
				if (myPiece == selPiece) {
					g.setColor(SELECTED_PIECE_BORDER_COLOR);
					g.fillRect(
							w/2 - (PIECE_RADIUS + MASTER_BORDER + SELECTED_PIECE_BORDER_THICKNESS),
							h/2 - (PIECE_RADIUS + MASTER_BORDER + SELECTED_PIECE_BORDER_THICKNESS),
							2*(PIECE_RADIUS + MASTER_BORDER + SELECTED_PIECE_BORDER_THICKNESS),
							2*(PIECE_RADIUS + MASTER_BORDER + SELECTED_PIECE_BORDER_THICKNESS)
						);
				}
				
				//color master border
				if (tp == PieceType.MASTER) {
					g.setColor(playerNumber == 2 ? Color.WHITE : Color.BLACK);
					g.fillRect(
							w/2 - (PIECE_RADIUS+MASTER_BORDER),
							h/2 - (PIECE_RADIUS+MASTER_BORDER),
							2*(PIECE_RADIUS+MASTER_BORDER),
							2*(PIECE_RADIUS+MASTER_BORDER)
						);
				}
				
				//color piece square
				g.setColor(playerNumber == 1 ? Color.WHITE : Color.BLACK);
				g.fillRect(
						w/2 - PIECE_RADIUS,
						h/2 - PIECE_RADIUS,
						2 * PIECE_RADIUS,
						2 * PIECE_RADIUS
					);
			}
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(70, 70);
		}	
	}
}