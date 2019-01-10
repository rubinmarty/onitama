public class Move extends Vector {
	public Move(int x, int y) {
		super(x, y);
	}
		
	public Move(Vector v) {
		super(v.getX(), v.getY());
	}
	
	private Move relativeMoveHelper(int direction) {
		switch (direction) {
			case 1:
				return this;
			case 2:
				return new Move(this.flip());
			case 3:
				return new Move(this.rotate("CCW"));
			default:
				throw new RuntimeException();
		}
	}
		
	public RelativeMove getRelativeMove(int direction) {
		return new RelativeMove(this, direction);
	}
		
	class RelativeMove extends Move {
		private Move originalMove;
		private RelativeMove(Move move, int direction) {
			super(move.relativeMoveHelper(direction));
			this.originalMove = move;
		}
		public Move getOriginalMove() {
			return originalMove;
		}
	}
}
	