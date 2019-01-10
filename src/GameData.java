import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameData {
	final public Player p1;
	final public Player p2;
	
	private Piece[][] boardData = new Piece[5][5];
	public BoardDataWrapper readOnlyBoardData = new BoardDataWrapper(boardData);
	
	final static private MoveSet[] MOVE_NAMES = MoveSet.values();
	
	private volatile Player activePlayer;
	private volatile MoveSet centerMoveSet;
	
	public volatile MoveSet selCard;
	public volatile Piece selPiece;
	
	public GameData(PlayerType pt1, PlayerType pt2) {		
		//create a deck and shuffle it
		List<MoveSet> allMoves = Arrays.asList(MOVE_NAMES);
		Collections.shuffle(allMoves);
						
		//create players and hand out the cards	
		this.p1 = new Player(1, allMoves.get(0), allMoves.get(1));
		this.p2 = new Player(2, allMoves.get(2), allMoves.get(3));
		centerMoveSet = allMoves.get(4);
		
		//create and link player souls
		Soul s1 = null;
		Soul s2 = null;
		switch (pt1) {
		case HUMAN:
			break;
		case EASYAI:
			s1 = new EasyRobotSoul(this);
			break;
		case MEDIUMAI:
			s1 = new MediumRobotSoul(this);
			break;
		}
		switch (pt2) {
		case HUMAN:
			break;
		case EASYAI:
			s2 = new EasyRobotSoul(this);
			break;
		case MEDIUMAI:
			s2 = new MediumRobotSoul(this);
			break;
		}
		
		p1.link(s1);
		p2.link(s2);
		
		//set active player to randomly chosen player
		this.activePlayer = (new Player[] {this.p1, this.p2})[new Random().nextInt(2)];
		
		//start recording the replay
		try {
			FileWriter fw = new FileWriter("myReplay.txt");
			for (int i = 0; i<5; i++) {
				fw.write(allMoves.get(i) + " ");
			}
			fw.write("\n");
			fw.write(activePlayer == p1 ? "Player 1" : "Player 2");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.updateBoardData();
	}
	
	public GameData(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String[] allMoves = br.readLine().split(" ");
		
		//create players and hand out the cards	
		this.p1 = new Player(1, MoveSet.valueOf(allMoves[0]), MoveSet.valueOf(allMoves[1]));
		this.p2 = new Player(2, MoveSet.valueOf(allMoves[2]), MoveSet.valueOf(allMoves[3]));
		centerMoveSet = MoveSet.valueOf(allMoves[4]);
		
		String firstPlayer = br.readLine();
		this.activePlayer = firstPlayer.equals("Player 1") ? p1 : p2;
		
		Soul s = new FileSoul(this, br);
		p1.link(s);
		p2.link(s);
		
		this.updateBoardData();
	}
	
	public class BoardDataWrapper {
		private Piece[][] boardData;
		
		private BoardDataWrapper(Piece[][] boardData) {
			this.boardData = boardData;
		}
		
		public Piece getPiece(int x, int y) {
			return boardData[x][y];
		}
	}
	
	public void updateBoardData() {
		for (int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				boardData[i][j] = null;
			}
		}
		for (Piece p : getAllPieces(p1, p2)) {			
			boardData[p.getPosition().getX()][p.getPosition().getY()] = p;
		}
	}

	public Player getActivePlayer() {
		return activePlayer;
	}
	
	public Player getOtherPlayer(Player p) {
		if (p == p1) {
			return p2;
		} else if (p == p2) {
			return p1;
		} else {
			throw new RuntimeException();
		}
	}
	
	public MoveSet getCenterMoveSet() {
		return centerMoveSet;
	}
	
	public MoveSet getMoveSet(int ID) {		
		switch(ID) {
		case 0:
			return p1.moveSets.getFirst();
		case 1:
			return p1.moveSets.getLast();
		case 2:
			return p2.moveSets.getFirst();
		case 3:
			return p2.moveSets.getLast();
		case 4:
			return centerMoveSet;
		default:
			throw new RuntimeException();
		}
	}
	
	public boolean occupiesSpace(LinkedList<Piece> pl, Vector v) {
		for (Piece p : pl) {
			if (p.getPosition().equals(v)) {
				return true;
			}
		}
		return false;
	}
	
	public Piece getOccupyingPiece(LinkedList<Piece> pl, Vector v) {
		for (Piece p : pl) {
			if (p.getPosition().equals(v)) {
				return p;
			}
		}
		return null;
	}
	
	public LinkedList<Piece> getAllPieces(Player... players) {
		LinkedList<Piece> tgt = new LinkedList<Piece>();
		for (Player p : players) {
			tgt.addAll(p.pieces);
		}
		return tgt;
	}
	
	public Boolean canSlide(Piece p, Move m) {
		Vector newPosition = p.getPosition().add(m);
		int newX = newPosition.getX();
		int newY = newPosition.getY();
		return (newX >= 0 && newY >= 0 && newX < 5 && newY < 5 &&
				!occupiesSpace(getAllPieces(p.owner), newPosition));
	}
	
	public Play getPlayIfValid(MoveSet card, Vector tgtSpace, Piece piece, Player player) {
		if (card==null || tgtSpace == null || piece == null || player == null) {
			return null;
		}
		
		Vector curSpace = piece.getPosition();
		Move move = new Move(tgtSpace.add(curSpace.flip()));
		Move.RelativeMove relMove = new Move(move.getRelativeMove(player.playerNumber)).getRelativeMove(player.playerNumber);
				
		Play play = new Play(card, relMove, piece, player);
		if (play.isValidPlay(this)) {
			return play;
		} else {
			return null;
		}
	}
	
	public void swapCards(Play play) {
		getActivePlayer().gainCenterCard(centerMoveSet, play.moveSet);
		this.centerMoveSet = play.moveSet;
	}
	
	public void passTurn() {
		activePlayer = getOtherPlayer(activePlayer);
	}
	
	public boolean checkEndzoneVictory(Player player) {
		Vector endzone = new Vector(2, 4 - player.homeRow);
		Piece potentialWinner = getOccupyingPiece(getAllPieces(player), endzone);
		if (potentialWinner != null && potentialWinner.pieceType == PieceType.MASTER) {
			return true;
		} else {
			return false;
		}
	}
}