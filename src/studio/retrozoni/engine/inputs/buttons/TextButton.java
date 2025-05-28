package studio.retrozoni.engine.inputs.buttons;

import studio.retrozoni.engine.graphics.FontHandler;
import studio.retrozoni.engine.inputs.Keyboard;
import studio.retrozoni.engine.inputs.Mouse;
import studio.retrozoni.engine.inputs.Mouse_Button;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.engine.sound.Sound;
import studio.retrozoni.engine.sound.Sounds;
import studio.retrozoni.engine.tools.Responsive;

import java.awt.*;

public class TextButton extends Button {
	
	private boolean selected;
	private boolean sent;
	private StringBuilder word;
	private int index_write;
	
	public TextButton(String Name, int x_per, int y_per, Responsive parent, int size) {
		super(Name, x_per, y_per, parent, size);
		word = new StringBuilder();
	}

	public void setText(String[] last) {
		for(String s : last) {
			word.append(last);
		}
	}
	
	private void writing(char[] Caracteres) {
		if(Mouse.clickOn(Mouse_Button.LEFT, getBounds())) {
			selected = !selected;
			Keyboard.getKeyChar();
		}else if(Mouse.pressing(Mouse_Button.LEFT) && !getBounds().contains(Mouse.getX(), Mouse.getY()))
			selected = false;
		if(!selected)
			return;
		barSystem();
		if(Keyboard.KeyPressed("Space")) {
			word.insert(index_write, " ");
			//TODO trocar o som!
			Sound.play(Sounds.Click);
			index_write++;
			return;
		}
		if(Keyboard.KeyPressed("Back_Space") && index_write > 0) {
			word.delete(index_write-1, index_write);
			//TODO trocar o som!
			Sound.play(Sounds.Click);
			index_write--;
			return;
		}
		char keyChar = Caracteres == null ? Keyboard.getKeyChar() : Keyboard.getKeyChar(Caracteres);
		if(keyChar != Keyboard.NONE) {
			word.insert(index_write, keyChar);
			//TODO trocar o som!
			Sound.play(Sounds.Click);
			index_write++;
		}
		if(Keyboard.KeyPressed("Enter")) {
			if(!word.isEmpty()) {
				sent = true;
				this.selected = false;
			}else {
				this.selected = false;
			}
		}
	}
	
	public boolean isSelect() {
		return this.selected;
	}
	
	public void setSelect(boolean selected) {
		this.selected = selected;
	}
	
	public String[] getText(char[] Caracteres) {
		writing(Caracteres);
		if(sent) {
			sent = false;
			String[] value = buildCommands();
			word = new StringBuilder();
			index_write = word.length();
			return value;
		}
		return null;
	}
	
	public String readText(char[] Caracteres) {
		writing(Caracteres);
		return word.toString().strip();
	}
	
	private void barSystem() {
		if(Keyboard.KeyPressed("Left")) {
			if(index_write > 0)
				index_write--;
		}else if(Keyboard.KeyPressed("Right")) {
			if(index_write < word.length())
				index_write++;
		}
	}
	
	private String[] buildCommands() {
		String values = word.toString().strip();
		return values.split(" ");
	}
	
	private String getWord() {
		return (word.isEmpty()) ? Name : word.toString();
	}
	
	public Rectangle getBounds() {
		return this.getResponsive().getBounds();
	}
	
	private void refresh() {
		String value = getWord();
		int width = FontHandler.getWidth(value, font) + margin;
		int height = FontHandler.getHeight(value, font) + margin;
		this.getResponsive().getBounds().setSize(width, height);
		this.getResponsive().setPosition();
	}
	
	public void render(Graphics2D g) {
		refresh();
		g.setStroke(new BasicStroke(Engine.SCALE));
		g.setFont(font);
		g.setColor(Theme.Tertiary);
		g.fillRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
		g.setColor(Theme.Primary);
		g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
		String value = getWord();
		int h = FontHandler.getHeight(value, font);
		if(word.isEmpty())
			g.setColor(Theme.Secondary);
		else
			g.setColor(Theme.Primary);
		g.drawString(value, getBounds().x + margin/2, getBounds().y + margin/2 + h);
		g.setColor(Theme.Primary);
		if(selected)
			g.fillRect(getBounds().x + (FontHandler.getWidth(word.substring(0, index_write), font)) + margin/2, getBounds().y + margin/2, Engine.SCALE, h);
	}
}
