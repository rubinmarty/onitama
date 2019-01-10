import java.awt.Color;

@SuppressWarnings("serial")
public class RotatingButton extends Button {
	private String[] texts;
	private int i = 0;
	
	public RotatingButton(String[] texts, Color color, int w, int h) {
		super(texts[0], color, w, h);
		this.texts = texts;
	}
	
	@Override
	public void myEvent() {
		i++;
		i %= texts.length;
		this.setText(texts[i]);
		myEvent(texts[i]);
	}
	
	public void myEvent(String currText) {
	}
}