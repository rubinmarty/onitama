public class Play {
	public MoveSet moveSet;
	public Move.RelativeMove relMove;
	public Piece piece;
	public Player player;
	
	public Play(MoveSet moveSet, Move.RelativeMove relMove, Piece piece, Player player) {
		this.moveSet = moveSet;
		this.relMove = relMove;
		this.piece = piece;
		this.player = player;
	}
		
	public boolean isValidPlay(GameData data) {
		return (
				data.getActivePlayer() == player
				&& moveSet.contains(new Move(relMove.getOriginalMove()))
				&& player.owns(piece)
				&& player.owns(moveSet)
				&& data.canSlide(piece, relMove)
				);
	}
}