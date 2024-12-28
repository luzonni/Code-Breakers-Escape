package com.coffee.objects.tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.Directions;
import com.coffee.objects.Variables;
import com.coffee.objects.entity.Entity;

public class Repellent extends Tile {
	
	public static BufferedImage sprite;

	public Repellent(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("repellent", Engine.Color_Primary)[0];
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
			if(this.collidingWith(entity) && entity.getVar(Variables.Alive)) {
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
