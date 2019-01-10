import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class Button extends JPanel {
	private Color color;
	private JLabel label;
	
	private Border defaultBorder;
	private Border selectedBorder;
	private boolean isSelected;
	
	{
		this.setLayout(new GridLayout(1,1));
		isSelected = false;
	}
	
	public Button(String text, Color color, int w, int h) {
		this.label = new JLabel(text);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(label.getFont().deriveFont((float) h*2/3));
		this.add(label);
		
		this.color = color;
		Color darkColor = color.darker().darker();
		Color lightColor = color.brighter().brighter();
		
		
		this.defaultBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED, darkColor, lightColor);
		this.selectedBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED, darkColor, lightColor);
		
		this.setBorder(defaultBorder);
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Button.this.setBorder(selectedBorder);
				Button.this.isSelected = true;
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Button.this.setBorder(defaultBorder);
				Button.this.isSelected = false;
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (Button.this.isSelected) {
					Button.this.myEvent();
				}
				Button.this.setBorder(defaultBorder);
				Button.this.isSelected = false;
			}
		});
		
		this.setMinimumSize(new Dimension(w,h));
		this.setPreferredSize(new Dimension(w,h));
	}
	
	public void myEvent() {
	}
	
	public void setText(String text) {
		this.label.setText(text);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(this.color);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
}