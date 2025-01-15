package com.coffee.objects.tiles;

import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.Theme;

public class Wall extends Tile {
	
	protected static BufferedImage[] sprite;
	protected static int index;
	
	public Wall(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null) {
			sprite = getSprite("wall", Theme.Primary);
			index = Engine.RAND.nextInt(sprite.length);
		}
		this.setSolid(true);
	}
	
	@Override
	public BufferedImage getSprite() {
		return sprite[index];
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}
	
}
