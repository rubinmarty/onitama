class PlaySubmitter {
	private GameEngine ge;
	private boolean active = true;
	
	public PlaySubmitter(GameEngine ge) {
		this.ge = ge;
	}
	
	public void submitPlay(Play p) {
		if (active) {
			this.ge.handlePlay(p, this);
		}
	}
	
	public void terminate() {
		this.active = false;
	}
}