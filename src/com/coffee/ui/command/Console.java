package com.coffee.ui.command;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.coffee.inputs.Keyboard;
import com.coffee.inputs.buttons.TextButton;
import com.coffee.exceptions.ConsoleError;
import com.coffee.graphics.FontG;
import com.coffee.graphics.SpriteSheet;
import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.main.tools.Responsive;

public class Console {
	
	private final Chat chat;

	protected TextButton textButton;
	private BufferedImage icon;
	private final BufferedImage arrow;

	public Console() {
		chat = new Chat(FontG.font(10 * Engine.SCALE));
		textButton = new TextButton("Command", 50, 95, null, 10);
		arrow = loadArrow();
	}

	private BufferedImage loadArrow() {
		SpriteSheet sheet = new SpriteSheet(Engine.ResPath+"/ui/arrow.png", Engine.SCALE);
		sheet.replaceColor(Theme.PRIMARY, Theme.Primary.getRGB());
		sheet.replaceColor(Theme.SECONDARY, Theme.Secondary.getRGB());
		sheet.replaceColor(Theme.TERTIARY, Theme.Tertiary.getRGB());
		return sheet.getImage();
	}
	
	public void print(String value, boolean write) {
		if(write)
			value = "%auto/"+value;
		chat.add(value);
		chat.tick();
	}

	public void setIcon(BufferedImage icon) {
		int rw = icon.getWidth() % 16;
		int rh = icon.getHeight() & 16;
		if(rw != 0 && rh != 0)
			throw new RuntimeException("Icon don't be a box");
		this.icon = icon;
	}

	public void cleatIcon() {
		this.icon = null;
	}

	public void clearChat() {
		chat.clear();
	}

	public boolean isWriting() {
		return chat.isWriting();
	}

	public void tick() {
		Keyboard.Writing(textButton.isSelect());
		String[] commands = textButton.getText(null);
		if(commands != null) {
			try {
				String Callback = Engine.UI.getReceiver().giveCommand(commands);
				if(!Callback.isBlank()) {
					print(Callback, true);
				}
			}catch (ConsoleError e) {
				print(e.getMessage(), true);
			}
		}
		if(Keyboard.KeyPressed("Enter") && !textButton.isSelect()) {
			textButton.setSelect(true);
		}
		chat.tick();
	}
	
	public void render(Graphics2D g) {
		textButton.render(g);
		chat.render(g);
		renderIcon(g);
	}

	private void renderIcon(Graphics2D g) {
		if(this.icon == null)
			return;
		Responsive res = Responsive.createPoint(textButton.getResponsive(), -1, 0);
		int x = res.getBounds().x - icon.getWidth()/2 - Engine.SCALE *23;
		int y = res.getBounds().y - icon.getHeight()/2;
		int w = icon.getWidth();
		int h = icon.getHeight();
		g.setColor(Theme.Primary);
		g.drawRect(x - Engine.SCALE *2, y - Engine.SCALE *2, w + Engine.SCALE *3, h + Engine.SCALE *3);
		g.drawImage(this.arrow, x + w/2 + this.arrow.getWidth()/2, y, null);
		g.drawImage(this.icon, x, y, null);
	}

}
