package studio.retrozoni.engine.inputs.buttons;

import studio.retrozoni.engine.graphics.FontHandler;
import studio.retrozoni.engine.inputs.Mouse;
import studio.retrozoni.engine.inputs.Mouse_Button;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.engine.sound.Sound;
import studio.retrozoni.engine.sound.Sounds;
import studio.retrozoni.engine.tools.Responsive;

import java.awt.*;

public class Button {
	
	protected String Name;
	protected Responsive P;
	public Font font;
	protected int margin;

	public Button(String Name, int x_per, int y_per, Responsive parent, int size) {
		this.font = FontHandler.font("septem", Engine.SCALE *size);
		this.Name = Name;
		margin = size * 3;
		int W = FontHandler.getWidth(this.Name, this.font) + margin;
		int H = FontHandler.getHeight(this.Name, this.font) + margin;
		Rectangle rec = new Rectangle(W, H);
		this.P = Responsive.createRectangle(parent, rec, x_per, y_per);
	}

	public void setName(String newName) {
		this.Name = newName;
		int W = FontHandler.getWidth(this.Name, this.font) + margin;
		int H = FontHandler.getHeight(this.Name, this.font) + margin;
		this.P.setSize(W, H);
	}
	
	public Responsive getResponsive() {
		return this.P;
	}

	public Rectangle getBounds() {
		return this.P.getBounds();
	}
	
	public boolean function() {
		if(Mouse.clickOn(Mouse_Button.LEFT, getBounds())) {
			Sound.play(Sounds.Click);
			return true;
		}
		return false;
	}
	
	public void render(Graphics2D g) {
		if(this.P == null)
			return;
		this.getResponsive().setPosition();
		boolean over = Mouse.On_Mouse(getBounds());
		float stroke = Engine.SCALE * 1.75f;
		g.setStroke(new BasicStroke(stroke));
		g.setFont(this.font);
		if(over) {
			g.setColor(Theme.Secondary);
			g.fillRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
		}
		g.setColor(Theme.Primary);
		g.drawString(this.Name, getBounds().x + margin/2, getBounds().y + getBounds().height/2 + margin/3);
		g.setColor(Theme.Primary);
		g.drawRect((int)(getBounds().x + stroke/2), (int)(getBounds().y + stroke/2), (int)(getBounds().width - stroke), (int)(getBounds().height - stroke));
	}
}
