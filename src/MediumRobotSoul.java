import java.util.LinkedList;
import java.util.List;

class MediumRobotSoul implements Soul {
	private Player myPlayer;
	private GameData data;
	
	public MediumRobotSoul(GameData data) {
		this.data = data;
	}
	
	public void getAction(PlaySubmitter ps) {		
		Play p = null;
		List<Play> plays = getAllLegalPlays();
		
		System.out.println("Looking for winning play.");
		p = getWinningPlay(plays);
		if (p == null) {
			System.out.println("No winning play found. Trying capture play.");
			p = getCapturePlay(plays);
			if (p == null) {
				System.out.println("No capture play found. Trying random play.\n");
				p = getRandomPlay(plays);
			}
		}
			
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ps.submitPlay(p);
		return;
	}
	
	private List<Play> getAllLegalPlays() {
		List<Play> allPlays = new LinkedList<Play>();
		for (Piece piece : myPlayer.pieces) {
			for (MoveSet ms : myPlayer.moveSets) {
				for (Move m : ms) {
					Play p = new Play(ms, m.getRelativeMove(myPlayer.playerNumber), piece, myPlayer);
					if (p.isValidPlay(data)) {
						allPlays.add(p);
					}
				}
			}
		}
		return allPlays;
	}
	
	private Play getWinningPlay(List<Play> allPlays) {
		for (Play p : allPlays) {
			Vector finalSpace = p.piece.getPosition().add(p.relMove);
			Player enemyPlayer = data.getOtherPlayer(myPlayer);
			
			//see if you can take their temple
			if (p.piece.pieceType == PieceType.MASTER) {
				Vector enemyTemple = new Vector(2, enemyPlayer.homeRow);
				if (finalSpace.equals(enemyTemple)) {
					return p;
				}
			}
			
			//see if you can capture their master
			Piece capturedPiece = data.getOccupyingPiece(data.getAllPieces(enemyPlayer), finalSpace);
			if (capturedPiece != null && capturedPiece.pieceType == PieceType.MASTER) {
				return p;
			}
		}
		return null;
	}
	
	private Play getCapturePlay(List<Play> allPlays) {
		for (Play p : allPlays) {
			Vector finalSpace = p.piece.getPosition().add(p.relMove);
			Player enemyPlayer = data.getOtherPlayer(myPlayer);
			
			//see if you can capture their master
			Piece capturedPiece = data.getOccupyingPiece(data.getAllPieces(enemyPlayer), finalSpace);
			if (capturedPiece != null) {
				return p;
			}
		}
		return null;
	}
	
	private Play getRandomPlay(List<Play> allPlays) {
		return allPlays.get(0);
	}
	
	public void attach(Player p) {
		this.myPlayer = p;
	}
}