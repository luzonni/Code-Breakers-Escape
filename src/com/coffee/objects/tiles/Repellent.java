package com.coffee.objects.tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import com.coffee.main.Theme;
import com.coffee.activity.game.Game;
import com.coffee.objects.Variables;
import com.coffee.objects.entity.Entity;

public class Repellent extends Tile {
	
	public static BufferedImage sprite;

	public Repellent(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("repellent", Theme.Primary)[0];
		setVar(Variables.Breakable, true);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite;
	}

	@Override
	public void tick() {
		List<Entity> entities = Game.getLevel().getEntities();
		for(int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if(entity.getOE().overTile() == this) {
				entity.getOE().setDirection(entity.getOE().getReverse());
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderTile(sprite, g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
