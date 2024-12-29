package com.coffee.command;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.coffee.Inputs.Keyboard;
import com.coffee.Inputs.Button.TextButton;
import com.coffee.graphics.FontG;
import com.coffee.graphics.SpriteSheet;
import com.coffee.main.Engine;
import com.coffee.main.tools.Responsive;

public class Console {
	
	private final Chat chat;
	private final List<String[]> lastCommands;

	protected TextButton textButton;
	private BufferedImage icon;
	private final BufferedImage arrow;

	public Console() {
		chat = new Chat(FontG.font(10 * Engine.GameScale));
		lastCommands = new ArrayList<String[]>();
		textButton = new TextButton("Command", 50, 95, null, 10);
		arrow = loadArrow();
	}

	private BufferedImage loadArrow() {
		SpriteSheet sheet = new SpriteSheet(Engine.ResPath+"/ui/arrow.png", Engine.GameScale);
		sheet.replaceColor(Engine.PRIMARY, Engine.Color_Primary.getRGB());
		sheet.replaceColor(Engine.SECONDARY, Engine.Color_Secondary.getRGB());
		sheet.replaceColor(Engine.TERTIARY, Engine.Color_Tertiary.getRGB());
		return sheet.getImage();
	}
	
	public void print(String value, boolean write) {
		if(write)
			value = "%auto/"+value;
		chat.add(value);
		chat.tick();
	}

	public void setIcon(BufferedImage icon) {
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
			String Callback = "Wrong command";
			try {
				Callback = Engine.UI.getReceiver().giveCommand(commands);
			}catch (Exception e) {
				e.printStackTrace();
			}
			if(!Callback.equals("")) {
				print(Callback, true);
				lastCommands.add(commands);
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
		int x = res.getBounds().x - icon.getWidth()/2 - Engine.GameScale*23;
		int y = res.getBounds().y - icon.getHeight()/2;
		int w = icon.getWidth();
		int h = icon.getHeight();
		g.setColor(Engine.Color_Primary);
		g.drawRect(x - Engine.GameScale*2, y - Engine.GameScale*2, w + Engine.GameScale*3, h + Engine.GameScale*3);
		g.drawImage(this.arrow, x + w/2 + this.arrow.getWidth()/2, y, null);
		g.drawImage(this.icon, x, y, null);
	}

}
