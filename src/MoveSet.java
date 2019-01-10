import java.util.Arrays;
import java.util.Iterator;

public enum MoveSet implements Iterable<Move> {
	TIGER(new Move(0,-2), new Move(0,1)),
	CRAB(new Move(-2,0), new Move(0,-1), new Move(2,0)),
	MONKEY(new Move(-1,1), new Move(1,1), new Move(1,-1), new Move(-1,-1)),
	CRANE(new Move(-1,1), new Move(1,1), new Move(0,-1)),
	DRAGON(new Move(-2,-1), new Move(2,-1), new Move(-1,1), new Move(1,1)),
	ELEPHANT(new Move(-1,-1), new Move(-1,0), new Move(1,-1), new Move(1,0)),
	MANTIS(new Move(-1,-1), new Move(1,-1), new Move(0,1)),
	BOAR(new Move(-1,0), new Move(0,-1), new Move(1,0)),
	FROG(new Move(-2,0), new Move(-1,-1), new Move(1,1)),
	GOOSE(new Move(-1,-1), new Move(-1,0), new Move(1,0), new Move(1,1)),
	HORSE(new Move(0,1), new Move(-1,0), new Move(0,-1)),
	EEL(new Move(-1,1),	new Move(1,0), new Move(-1,-1)),
	RABBIT(new Move(-1,1), new Move(1,-1), new Move(2,0)),
	ROOSTER(new Move(-1,1), new Move(-1,0), new Move(1,0), new Move(1,-1)),
	OX(new Move(0,1), new Move(1,0), new Move(0,-1)),
	COBRA(new Move(1,1), new Move(-1,0), new Move(1,-1));
	
	private Move[] myMoves;
	
	MoveSet(Move... myMoves) {
		this.myMoves = myMoves;
	}
	
	public boolean contains(Move m1) {
		for (Move m2 : myMoves) {
			if (m1.equals(m2)) {
				return true;
			}
		}
		return false;
	}
	
	public int size() {
		return myMoves.length;
	}
	
	public Move get(int i) {
		return myMoves[i];
	}
	
	@Override
	public Iterator<Move> iterator() {
		return Arrays.asList(myMoves).iterator();
	}
}