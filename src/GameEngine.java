import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class GameEngine {
	final public GameData data;
	final public GameField field;
	private PlaySubmitter currentPlaySubmitter = null;
	private PlaySubmitter publicPlaySubmitter;
	private Player winner = null;
	
	private boolean isReplay = false;
	
	public GameEngine(PlayerType pt1, PlayerType pt2) {
		this.publicPlaySubmitter = new PlaySubmitter(this);
		
		this.data = new GameData(pt1, pt2);
		
		this.field = new GameField(this.data, publicPlaySubmitter);
	}
	
	public GameEngine(File f) throws IOException {
		this.isReplay = true;
		
		this.publicPlaySubmitter = new PlaySubmitter(this);
		
		this.data = new GameData(f);
		
		this.field = new GameField(this.data, publicPlaySubmitter);
	}
	
	public synchronized void handlePlay(Play p, PlaySubmitter ps) {		
		//TODO: verify play is being sent by owner
		if (ps != currentPlaySubmitter && (data.getActivePlayer().hasSoul() || ps != publicPlaySubmitter)) {
			System.out.println("An illegal playsubmitter is being used.");
			return;
		}
		
		//check that play is valid
		if (p.isValidPlay(data)) {
		
			Vector startingPosition = p.piece.getPosition();
			
			//update board-state
			slidePiece(p);
			data.swapCards(p);
			data.passTurn();
			data.updateBoardData();
			
			if (ps == publicPlaySubmitter) {
				this.data.selCard = null;
				this.data.selPiece = null;
			}
			
			Vector finalPosition = p.piece.getPosition();
			
			if (!isReplay) {
				try {
					FileWriter fw = new FileWriter("myReplay.txt", true);
					fw.write( "\n"
							+ Integer.toString(startingPosition.getX()) + " "
							+ Integer.toString(startingPosition.getY()) + " "
							+ Integer.toString(finalPosition.getX()) + " "
							+ Integer.toString(finalPosition.getY()) + " "
							+ p.moveSet.toString()
							);
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			//redraw everything
			field.repaint();
			
			//check for victory
			if (this.winner != null) {
				checkForVictory();
				return;
			}
		} else {
			System.out.println("Illegal play received.");
		}
		
		//notify next player in new thread
		getAction();
	}
	
	public void start() {
		//field.repaint();
		getAction();
	}
	
	public void getAction() {
		this.currentPlaySubmitter = new PlaySubmitter(this);
		
		new Thread() { 
			public void run() {
				data.getActivePlayer().getAction(GameEngine.this.currentPlaySubmitter);
			}
		}.start();
		
	}
	
	public void slidePiece(Play play) {
		Piece piece = play.piece;
		Move move = play.relMove;
		Player player = play.player;
		
		// update piece's position
		piece.setPosition(piece.getPosition().add(move));
		
		// gets the Non-Active Player, then gets a list of his pieces, then checks
		// if any of them are on the captured space and removes them if so
		Player nap = data.getOtherPlayer(player);
		LinkedList<Piece> enemyPieces = data.getAllPieces(nap);
		Piece deadPiece = data.getOccupyingPiece(enemyPieces, piece.getPosition()); //can return null
		if (deadPiece != null) {
			kill(deadPiece);
		}
		
		//updates the 2D array used for rendering the pieces
		data.updateBoardData();
				
		//checks if the moving piece just entered the goal tile and triggered a win
		if (piece.pieceType == PieceType.MASTER
				&& data.checkEndzoneVictory(player)) {
			this.winner = player;
		}
	}
	
	private void kill(Piece p) {
		System.out.println("Piece captured");
		p.owner.pieces.remove(p);
		if (p.pieceType == PieceType.MASTER) {
			this.winner = data.getActivePlayer();
		}
	}
	
	private void checkForVictory() {
		if (this.winner != null) {
			triggerVictory(this.winner);
		}
	}
	
	private void triggerVictory(Player p) {
		System.out.println("Player " + p.playerNumber + " wins");
		this.terminate();
		JOptionPane.showMessageDialog(null, "Player " + p.playerNumber + " wins");
	}
	
	public void terminate() {
		this.publicPlaySubmitter.terminate();
		this.currentPlaySubmitter.terminate();
	}
}