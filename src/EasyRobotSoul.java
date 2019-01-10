import java.util.Random;

class EasyRobotSoul implements Soul {
	private Player myPlayer;
	private GameData data;
	
	public EasyRobotSoul(GameData data) {
		this.data = data;
	}
	
	public void getAction(PlaySubmitter ps) {		
		//System.out.println("Player " + this.myPlayer.playerNumber + " is thinking.");
		while(true) {
			Random r = new Random();
			MoveSet allMoves = this.myPlayer.moveSets.get( r.nextInt( this.myPlayer.moveSets.size() ));
			Move move = allMoves.get( r.nextInt( allMoves.size() ));
			Move.RelativeMove relMove = move.getRelativeMove(myPlayer.playerNumber);
			Piece piece = this.myPlayer.pieces.get( r.nextInt( myPlayer.pieces.size() ));
			Play play = new Play(allMoves, relMove, piece, myPlayer);
			if (play.isValidPlay(this.data)) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ps.submitPlay(play);
				return;
			}
		}
	}

	public void attach(Player p) {
		this.myPlayer = p;
	}
}