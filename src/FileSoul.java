import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileSoul implements Soul {
	private GameData data;
	private BufferedReader br;
	
	public FileSoul(GameData data, BufferedReader br) throws FileNotFoundException {
		this.data = data;
		this.br = br;
	}
	
	@Override
	public void getAction(PlaySubmitter ps) {
		try {
			String nextLine = br.readLine();
			//System.out.println(nextLine);
			if (nextLine == null) {
				return;
			}
			
			String[] myMoveInfo = nextLine.split(" ");
			if (myMoveInfo.length != 5) {
				throw new IOException();
			}
			
			Player player = data.getActivePlayer();
			
			Vector pieceStartPosition = new Vector(Integer.parseInt(myMoveInfo[0]),Integer.parseInt(myMoveInfo[1]));
			Vector pieceEndPosition = new Vector(Integer.parseInt(myMoveInfo[2]),Integer.parseInt(myMoveInfo[3]));
			Move move = new Move(pieceEndPosition.add(pieceStartPosition.flip()));
			Move.RelativeMove relMove = move.getRelativeMove(player.playerNumber).getRelativeMove(player.playerNumber);
			
			Piece piece = data.getOccupyingPiece(data.getAllPieces(data.p1, data.p2), pieceStartPosition);
			MoveSet moveSet = MoveSet.valueOf(myMoveInfo[4]);
			Play play = new Play(moveSet, relMove, piece, player);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			ps.submitPlay(play);
			return;
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void attach(Player p) {
	}

}
