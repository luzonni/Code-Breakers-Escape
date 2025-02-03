package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import com.coffee.main.Theme;
import com.coffee.activity.game.Game;
import com.coffee.objects.Variables;

public class Button extends Entity {
	
	private static BufferedImage[] sprite;
	private boolean pressed;
	private int timer;

	public Button(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("button", Theme.Primary);
		setEffect(Variables.Movable);
		setDepth(0);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[pressed ? 1 : 0];
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	private void click(boolean bool) {
		if(this.pressed != bool) {
			this.pressed = bool;
		}
	}

	private boolean allButtonsPressed() {
		List<Entity> entities = Game.getLevel().getEntities();
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if(e instanceof Button button && !button.isPressed()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void tick() {
		if(allButtonsPressed()) {
			this.pressed = true;
			return;
		}
		for(Entity e : Game.getLevel().getEntities())
			if(e != this && e.collidingWith(this)) {
				click(true);
				return;
			}
		
		if(pressed && timer++ > 10) {
			timer = 0;
			click(false);
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		renderEntity(getSprite(), g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
