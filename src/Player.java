import java.util.LinkedList;

public class Player {
	public int playerNumber;
	public LinkedList<MoveSet> moveSets;
	public LinkedList<Piece> pieces;
	final public int homeRow;
	private Soul mySoul;
		
	public Player(int j, MoveSet ms1, MoveSet ms2) {
		this.playerNumber = j;
		
		this.moveSets = new LinkedList<MoveSet>();
		moveSets.add(ms1);
		moveSets.add(ms2);
		
		this.pieces = new LinkedList<Piece>();
		if (this.playerNumber == 1) {
			this.homeRow = 4;
		} else if (this.playerNumber == 2) {
			this.homeRow = 0;
		} else {
			throw new RuntimeException();
		}
		
		for (int i = 0; i < 5; i++) {
			PieceType type = (i==2 ? PieceType.MASTER : PieceType.PAWN);
			Piece p = new Piece(i, homeRow, this, type);
			this.pieces.add(p);
		}
	}
	
		
	public void gainCenterCard(MoveSet newCard, MoveSet oldCard) {
		this.moveSets.remove(oldCard);
		this.moveSets.add(newCard);
	}
		
	public boolean owns(Piece piece) {
		return pieces.contains(piece);
	}
	
	public boolean owns(MoveSet moveSet) {
		return moveSets.contains(moveSet);
	}
	
	public boolean hasSoul() {
		return this.mySoul != null;
	}
	
	public void link(Soul s) {
		if (s == null) {
			return;
		}
		this.mySoul = s;
		s.attach(this);
	}
		
	public void getAction(PlaySubmitter ps) {
		if (this.mySoul == null) {
			return;
		}
		this.mySoul.getAction(ps);
	}
}