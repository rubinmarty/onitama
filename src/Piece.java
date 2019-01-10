public class Piece {
	private Vector position;
	protected final Player owner;
	protected final PieceType pieceType;
	
	public Piece(int i, int j, Player owner, PieceType pieceType) {
		this.setPosition(new Vector(i,j));
		this.owner = owner;
		this.pieceType = pieceType;
	}
	
	public Vector getPosition() { 
		return position.clone();
	}

	public void setPosition(Vector position) {
		this.position = position;
	}
}