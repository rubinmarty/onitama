class GameField {
	public Board board;
	public Hand hand1;
	public Hand hand2;
	public Hand centerHand;
	
	public GameField(GameData data, PlaySubmitter ps) {
		hand1 = new Hand(1, data);
		hand2 = new Hand(2, data);
		centerHand = new Hand(3, data);
		board = new Board(data, ps);
	}
	
	public void repaint() {
		board.repaint();
		hand1.repaint();
		hand2.repaint();
		centerHand.repaint();
	}
}