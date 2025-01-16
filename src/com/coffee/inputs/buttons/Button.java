package com.coffee.inputs.buttons;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.coffee.inputs.Mouse;
import com.coffee.inputs.Mouse_Button;
import com.coffee.graphics.FontG;
import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.main.sound.Sound;
import com.coffee.main.sound.Sounds;
import com.coffee.main.tools.Responsive;

public class Button {
	
	protected String Name;
	protected Responsive P;
	public Font font;
	protected int margin;

	public Button(String Name, int x_per, int y_per, Responsive parent, int size) {
		this.font = FontG.font(Engine.SCALE *size);
		this.Name = Name;
		margin = size * 3;
		int W = FontG.getWidth(this.Name, this.font) + margin;
		int H = FontG.getHeight(this.Name, this.font) + margin;
		Rectangle rec = new Rectangle(W, H);
		this.P = Responsive.createRectangle(parent, rec, x_per, y_per);
	}

	public void setName(String newName) {
		this.Name = newName;
		int W = FontG.getWidth(this.Name, this.font) + margin;
		int H = FontG.getHeight(this.Name, this.font) + margin;
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
